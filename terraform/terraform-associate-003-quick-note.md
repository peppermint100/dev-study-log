
## terraform version 
```hcl
terraform {
	required_providers {
		aws = {
		source = "hashicorp/aws"
		version ~> "5.36.0"
		}
	}
}
```
- 버전 표기에서 `~>` 는 가장 오른쪽 버전가 와일드 카드인 것을 의미한다 
  - ex) ~> “5.36.0이면 5.36.n 버전을 의미

## Terraform Plan 결과 기호 요약

| 기호 (Symbol) | 의미 | 설명 |
|---|---|---|
| + (더하기) | **생성 (Create)** ➕ | 이 리소스는 존재하지 않으므로 **새롭게 생성**됩니다. plan 결과에서 초록색으로 표시됩니다. |
| - (빼기) | **삭제 (Destroy)** ➖ | 코드에서 제거되었거나 destroy 대상으로 지정된 리소스입니다. **완전히 삭제**됩니다. plan 결과에서 붉은색으로 표시됩니다. |
| ~ (물결) | **변경 (In-place Update)** ✏️ | 기존 리소스를 삭제하지 않고 **설정값만 변경**합니다. 일반적으로 가장 안전한 변경입니다. (예: 태그 수정, 보안 그룹 규칙 추가) plan 결과에서 노란색으로 표시됩니다. |
| -/+ (빼기/더하기) | **삭제 후 재생성 (Destroy and Recreate)** 🔄 | 리소스의 핵심 속성(변경 불가 속성)이 바뀌어 **기존 리소스를 삭제하고 완전히 새로 만들어야만** 하는 경우입니다. 가장 큰 변화이며, 서비스 중단을 유발할 수 있어 주의가 필요합니다. (예: EC2 인스턴스 타입 변경) |

# Terraform Sentinel이란?

HCP Terraform(Cloud) 및 Terraform Enterprise에서 사용하는 **코드형 정책(Policy as Code) 프레임워크**이다. Terraform 코드가 조직의 규칙을 위반하지 않았는지 자동으로 검사한다.
* **목적**: 비용, 보안, 규정 준수 등 조직의 규칙을 코드로 정의하고 강제하는 데 사용.
* **실행 위치**: HCP Terraform의 plan과 apply 단계 사이에서 실행됨.
* **핵심 기능**: 정책을 위반하는 인프라 변경을 사전에 차단하여 안정성을 높인다.

```hcl
# enforce-t3-micro.sentinel
import "tfplan/v2" as tfplan

# t3.micro 타입만 허용하는 규칙 정의
main = rule {
    # plan에 포함된 모든 aws_instance 리소스를 찾음
    all_instances = filter tfplan.resource_changes as _, rc {
        rc.type is "aws_instance" and rc.mode is "managed"
    }

    # 모든 인스턴스의 instance_type이 "t3.micro"인지 검사
    all all_instances as _, i {
        i.change.after.instance_type is "t3.micro"
    }
}
```
- 위 파일을 git에 저장 후 HCP Terraform에서 세팅해서 사용

## terraform dynamic keyword
Terraform의 dynamic 블록은 리소스 내에서 반복되는 **중첩 블록(nested blocks)**을 동적으로 생성한다.

#### 기존 security group의 ingress 선언 방식
```hcl
resource "aws_security_group" "web" {
  name = "web-server-sg"

  # 규칙을 추가할 때마다 이 블록을 복사해야 함
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["1.2.3.4/32"]
  }
}
```
- 불필요하게 반복된다.

```hcl
ingress_rules = [
  { port = 80, cidr = "0.0.0.0/0" },
  { port = 443, cidr = "0.0.0.0/0" },
  { port = 22, cidr = "1.2.3.4/32" }
]

resource "aws_security_group" "web" {
  name = "web-server-sg"

  dynamic "ingress" {
    for_each = var.ingress_rules

    content {
      from_port   = ingress.value.port
      to_port     = ingress.value.port
      protocol    = "tcp"
      cidr_blocks = [ingress.value.cidr]
    }
  }
}
```
- dyncmic keyword를 사용하여 배열로부터 값을 추출하여 설정할 수 있다. 


## 테라폼 프로바이더 버전을 명시해야 하는 이유
Terraform 코어(Core)와 프로바이더(Provider)가 서로 다른 주기로 개발되고 배포된다. 프로바이더는 Terraform과 별개로 수시로 업데이트되며, 이 과정에서 기존 코드와 호환되지 않는 변경(Breaking Changes)이 발생할 수 있다.

AWS 프로바이더 5.0 버전에서 갑자기 특정 리소스의 필수 인수가 변경되거나 삭제될 수 있다. 버전을 고정하지 않으면, 나중에 terraform init을 실행했을 때 최신 버전의 프로바이더가 설치되어 잘 동작하던 코드가 갑자기 오류를 발생시킬 수 있다. 따라서 버전을 명시하여 이러한 예기치 않은 변경으로부터 코드를 보호해야 한다.


Terraform에서 .gitignore에 추가해야 할 파일
Terraform 프로젝트를 Git으로 관리할 때, 민감 정보나 불필요한 파일이 원격 저장소(GitHub 등)에 올라가는 것을 막기 위해 아래 파일들은 **.gitignore**에 반드시 추가해야 한다.

### terraform.tfstate / terraform.tfstate.backup
이유: 인프라의 모든 정보(ID, IP 주소, 비밀번호 등)가 담긴 가장 민감한 파일. 유출 시 인프라 전체가 탈취될 수 있음.

올바른 관리법: 원격 백엔드(Remote Backend)를 사용해 안전하게 관리해야 한다.

### .terraform/ 디렉터리
이유: terraform init 시 다운로드되는 프로바이더 플러그인을 저장하는 로컬 캐시 폴더. 용량이 크고 공유할 필요가 없으며, 각자 환경에서 자동으로 생성됨.

### *.tfvars 파일
이유: 데이터베이스 암호, API 키 등 민감한 변수를 저장하는 용도로 자주 사용되므로 Git에 절대 포함하면 안 됨.

### *.tfplan 파일
이유: plan 결과물로, tfstate처럼 인프라의 상세 정보가 포함될 수 있는 민감한 임시 파일이다.

terraform apply -refresh-only를 사용하는 경우
### 1. 긴급 수정(Hotfix) 후 상태를 정리할 때 

* **상황**: 서비스 장애가 발생하여 Terraform을 통하지 않고 AWS 콘솔에서 급하게 보안 그룹 규칙을 수동으로 변경했다.
* **문제**: Terraform의 상태 파일은 이 변경을 모르기 때문에, 다음 plan 실행 시 이 긴급 수정을 '잘못된 변경'으로 간주하고 원래대로 되돌리려고 한다.
* **해결**: terraform apply -refresh-only를 실행하여, 수동으로 변경된 내용을 상태 파일에 먼저 반영시킨다. 이렇게 하면 Terraform이 현실을 '인정'하게 되어, 다음 작업 시 긴급 수정을 덮어쓰는 실수를 방지할 수 있다.

### 2. 수동 변경(Drift)의 정확한 내용을 파악하고 싶을 때 

* **상황**: terraform plan을 실행했더니 예상치 못한 변경 사항이 너무 많이 나타나, 코드를 기준으로 무엇이 잘못된 건지 파악하기 어렵다.
* **문제**: 상태 파일이 최신이 아니어서, Terraform이 (코드) vs (옛날 상태)를 비교한 결과를 보여주기 때문이다.
* **해결**: terraform apply -refresh-only로 상태를 먼저 최신화한다. 그 후 다시 terraform plan을 실행하면, 이제는 (코드) vs (최신 현실)을 명확하게 비교해주므로 수동으로 변경된 부분이 무엇인지 정확히 파악할 수 있다.

### 3.terraform import 직후 상태를 보정할 때 

* **상황**: 기존에 수동으로 만든 리소스를 terraform import 명령어로 Terraform 관리 하에 편입시켰다.
* **문제**: import는 리소스의 모든 속성을 완벽하게 가져오지 못하는 경우가 있다.
* **해결**: import 직후 terraform apply -refresh-only를 실행하여, 실제 리소스의 모든 속성을 다시 한번 상세하게 읽어와 상태 파일을 완벽하게 만들어준다.


## terraform registry의 모듈 상세 정보에서 확인할 수 있는 것![](image.png)
- input
- output
- dependency
- resources: 이 모듈에 생성하는 리소스 종류

## TFLOG
Terraform의 TFLOG는 Terraform 실행 시 상세한 로그를 파일로 남기기 위해 사용하는 환경 변수이다. 이 로그는 Terraform의 내부 동작을 자세히 분석하고 문제를 디버깅하는 데 매우 유용하다.

* Terraform의 로깅 시스템을 제어하는 환경 변수이다.
* 설정 시 Terraform은 terraform.log라는 파일을 생성하고 실행 과정에 대한 상세한 로그를 기록한다.
* 로그 파일은 Terraform 명령이 실행되는 디렉터리에 생성된다.

### TFLOG 로그 레벨
TFLOG 환경 변수에 설정할 수 있는 값으로, 로그의 상세 수준을 결정한다. 아래로 갈수록 더 상세한 정보를 포함한다.
* **TRACE**: 가장 상세한 로그 레벨. Terraform의 모든 내부 동작과 통신 내용을 기록하여 디버깅에 가장 유용하다.
* **DEBUG**: TRACE보다 덜 상세하지만, 일반적인 문제 해결에 충분한 정보를 제공한다.
* **INFO**: 정보성 메시지를 기록한다.
* **WARN**: 경고 메시지를 기록한다.
* **ERROR**: 오류 메시지만 기록한다.

### TFLOG 사용법
TFLOG는 터미널에서 환경 변수로 설정하여 사용한다.
```bash
export TF_LOG=TRACE
terraform plan
```
- 위와 같이 설정하고 terraform 명령어를 실행하면, 해당 명령어의 실행 로그가 terraform.log 파일에 TRACE 레벨로 기록된다.
- 문제 분석이 끝나면 unset TF_LOG (Linux/macOS)로 설정을 해제하는 것이 좋다. 로그 파일이 매우 커질 수 있기 때문이다.

## locals
Terraform에서 locals는 **코드 내에서 재사용할 수 있는 지역 변수**를 선언하는 블록이다.
* **목적**: 복잡한 표현식을 단순화하거나, 코드 내에서 반복적으로 사용되는 값에 이름을 부여하여 가독성과 유지보수성을 높이는 데 사용.
* **특징**: variable과 달리, 사용자가 외부에서 값을 변경할 수 없다. 오직 .tf 파일 안에서만 정의하고 사용할 수 있는 내부용 값이다.

```hcl
# locals.tf
locals {
  # 여러 리소스에서 공통으로 사용할 태그 맵(map) 정의
  common_tags = {
    Owner   = "backend-team"
    Service = "web-app"
  }

  # 복잡한 표현식이나 조합된 이름에 별칭 부여
  instance_name = "app-server-${terraform.workspace}"
}

# main.tf
resource "aws_instance" "web" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = "t2.micro"

  # local.instance_name 값을 참조
  tags = merge(
    local.common_tags, # local.common_tags 값을 참조
    {
      Name = local.instance_name
    }
  )
}
```

## HCP Terraform Permissions
HCP Terraform의 권한(Permissions)은 **'누가(Who), 무엇을(What), 어디에서(Where)'** 할 수 있는지를 제어하는 **보안 및 협업 기능**이다.
여러 사용자가 함께 작업하는 환경에서, 팀이나 역할에 따라 인프라 변경 권한을 다르게 부여하여 실수를 방지하고 보안을 강화하는 것이 핵심 목표이다.

### 권한의 대상 (Who): 사용자(Users)와 팀(Teams)
* **Users**: 조직(Organization)에 초대된 개별 계정이다.
* **Teams**: 여러 사용자를 묶은 그룹 (예: dev-team, ops-team).
* **핵심**: 권한은 개별 사용자가 아닌 **팀(Team) 단위로 관리**하는 것이 베스트 프랙티스이다. 사용자를 팀에 소속시켜 권한을 일괄적으로 부여하고 관리한다.

### 권한이 적용되는 위치 (Where): 워크스페이스(Workspaces)
* 권한은 주로 **워크스페이스(Workspace)** 범위에 적용된다.
* 워크스페이스는 특정 인프라(예: 운영 DB)를 관리하는 독립된 공간이므로, 환경별로 접근 권한을 세밀하게 제어할 수 있다.
* **예시**: dev-team은 dev-webapp 워크스페이스에는 Admin 권한을 갖지만, prod-db 워크스페이스에는 Read 권한만 갖도록 설정할 수 있다.

|       |                                                           |
|-------|-----------------------------------------------------------|
| Read  | 워크스페이스 설정, 상태, 실행 결과 조회만 가능                               |
| Plan  | Read + terraform plan은 가능                                 |
| Write | Plan + apply 가능                                           |
| Admin | Write + 워크스페이스 설정 변경, 팀 접근 권한 관리, 워크스페이스 삭제 등 모든 관리 작업 가능 |
## credentials 설정 방식
### 프로바이더 블록에 직접 설정
#### provider 블록 안에 access_key나 secret_key 같은 자격 증명 인수를 직접 명시하는 방식이다.
* **A. 하드코딩 (Hard-coding)**: 코드에 값을 그대로 적는 방법으로, 매우 간단하지만 보안상 **절대 사용하면 안 되는** 위험한 방식이다. Git에 민감 정보가 그대로 노출된다.

```hcl
provider "aws" {
  region     = "ap-northeast-2"
  access_key = "AKIA..."
  secret_key = "supersecret..."
}
```

### 환경 변수 사용 (Environment Variables)

터미널 환경 변수(Environment Variable)에 자격 증명을 설정하면, 프로바이더가 자동으로 이를 인식하여 사용하는 방식이다.
* **동작 방식**: 각 프로바이더는 약속된 이름의 환경 변수를 찾는다. 예를 들어, AWS 프로바이더는 AWS_ACCESS_KEY_ID와 AWS_SECRET_KEY를 자동으로 읽어온다.
* **특징**: 코드에서 자격 증명을 완전히 분리할 수 있어 안전하며, CI/CD 파이프라인에서 널리 사용된다.

```bash
export AWS_ACCESS_KEY_ID="AKIA..."
export AWS_SECRET_KEY="..."
```

### 통합 서비스 사용 (Integrated Services)
AWS IAM 역할(Role)이나 Azure Managed Identity처럼, Terraform이 실행되는 환경(예: EC2 인스턴스)에 부여된 역할을 통해 자동으로 임시 자격 증명을 발급받는 방식이다.
* **동작 방식**: EC2 인스턴스에 IAM 역할이 할당되어 있다면, AWS 프로바이더는 AWS API를 통해 해당 역할에 대한 임시 보안 키를 자동으로 발급받아 사용한다.
* **특징**: Access Key 같은 **장기 자격 증명(Long-term credential)이 전혀 필요 없는** 가장 안전하고 권장되는 방식이다. 클라우드 환경에서 Terraform을 실행할 때 베스트 프랙티스이다.

네, 이 문제와 각 보기의 역할에 대해 요청하신 톤앤매너에 맞추어 설명해 드립니다.

### Terraform Provider
Terraform 코어와 외부 서비스(AWS, GCP 등)의 API 사이를 중계하는 플러그인이다. 프로바이더는 특정 API의 복잡한 호출 방식을 추상화하고, aws_instance와 같은 간단한 리소스 형태로 Terraform 사용자에게 노출하는 책임을 진다. 사용자가 HCL 코드로 "EC2 인스턴스를 만들어줘"라고 선언하면, AWS 프로바이더가 이를 실제 AWS API 호출로 번역하여 실행한다.

### Terraform Backend
Terraform이 생성한 인프라의 상태를 기록하는 terraform.tfstate 파일을 어디에 저장할지 정의하는 역할이다. S3, HCP Terraform 등이 백엔드로 사용될 수 있다. 백엔드는 상태 파일을 저장하고 잠그는(lock) 역할만 할 뿐, 외부 API와 직접 통신하여 리소스를 생성하지는 않는다.

### Terraform Provisioner
리소스가 **생성된 후**에 해당 리소스 내부에서 특정 스크립트를 실행하거나 파일을 복사하는 등의 추가 작업을 할 때 사용한다. 예를 들어, EC2 인스턴스가 만들어진 후에 remote-exec 프로비저너를 사용하여 웹 서버를 설치하는 스크립트를 실행할 수 있다. 이는 리소스 생성 후의 설정 단계이며, API와 상호작용하는 역할이 아니다.

### Terraform Configuration File
 .tf 확장자를 가진 파일들로, 사용자가 "어떤 인프라를 원하는지"를 코드로 선언하는 곳이다. 이것은 Terraform에 전달되는 **입력값**이지, API 상호작용을 처리하는 실행 주체가 아니다. 프로바이더는 이 설계도(설정 파일)를 읽어서 실제 행동으로 옮기는 역할을 한다.

### Terraform Sentinel
Terraform Cloud 및 Terraform Enterprise에서만 사용 가능한 정책 프레임워크이다. **Terraform이 인프라를 생성하거나 변경하기 전에, 특정 규칙(정책)을 충족하는지 검사하는 역할**을 한다. 코드에 문제가 있거나, 회사의 보안/비용 정책을 위반하는 변경을 사전에 차단하기 위해 사용한다.
#### 주요 특징
* **Terraform 통합**: Terraform 실행 계획(plan), 상태(state), 구성(configuration) 파일에 직접 접근하여 정책을 적용한다.
* **정책 언어**: Sentinel 자체의 독자적인 언어를 사용한다. 다른 프로그래밍 언어와 유사한 구문을 가지고 있어 학습이 비교적 쉽다.
* **적용 수준 (Enforcement Levels)**
  * **Advisory**: 정책 위반 시 경고만 알리고 실행을 중단하지 않는다.
  * **Soft-mandatory**: 정책 위반 시 관리자가 재정의(override)하여 실행을 계속할 수 있다.
  * **Hard-mandatory**: 정책 위반 시 실행을 무조건 중단하며, 재정의할 수 없다.
#### 보안 정책
- 네트워크 접근 제어: 특정 IP 주소 대역에서만 데이터베이스나 가상 머신에 접근할 수 있도록 강제한다.
  * *예시: SSH 포트(22)가 모든 IP(*0.0.0.0/0*)에 열리는 것을 방지한다.*
- 암호화 적용: 스토리지(S3 버킷, 데이터베이스 등) 생성 시 암호화 옵션이 반드시 활성화되도록 강제한다.
- IAM (Identity and Access Management) 정책: 과도한 권한을 가진 사용자나 역할이 생성되는 것을 막는다.

#### 비용 관리 정책
불필요한 비용 발생을 막고, 리소스 사용을 최적화하기 위한 규칙을 적용한다.
* 리소스 사양 제한: 고가의 가상 머신(Instance Type)이나 과도한 크기의 디스크를 생성하지 못하도록 제한한다.
  * *예시:* m5.24xlarge*와 같이 비싼 EC2 인스턴스 타입 사용을 금지한다.*
* 리소스 태그 강제: 모든 리소스에 비용 추적 및 관리를 위한 특정 태그(예: owner, project)가 반드시 포함되도록 강제한다.

#### 규정 준수 (Compliance) 정책
기업이 따라야 하는 특정 규정이나 표준(GDPR, HIPAA 등)을 준수하도록 규칙을 적용한다.
* **리전 제한**: 특정 리전(Region)에만 리소스를 생성하도록 강제한다.
  * *예시: 데이터 주권 문제로 유럽(eu-central-1) 리전에만 리소스를 배포하도록 한다.*
* **필수 로깅 설정**: 모든 서비스에 대해 감사 및 추적을 위한 로깅 기능이 활성화되도록 강제한다.

### Open Policy Agent (OPA)
오픈 소스 범용 정책 엔진이다. Terraform뿐만 아니라 Kubernetes, 마이크로서비스 등 다양한 시스템에 적용할 수 있다.
* **주요 특징**
  * **범용성**: 특정 플랫폼에 종속되지 않고, API를 통해 JSON 형식의 데이터를 입력받아 정책을 평가할 수 있다.
  * **정책 언어**: Rego라는 선언적 쿼리 언어를 사용한다. 데이터를 기반으로 규칙을 정의하고 쿼리하는 데 특화되어 있다.
  * **유연한 배포**: 사이드카(Sidecar) 프록시, 호스트 레벨 데몬, 라이브러리 등 다양한 형태로 배포하여 사용할 수 있다.


## terraform workspace
Terraform 워크스페이스(Workspace)는 동일한 구성 파일을 사용하여 여러 환경(예: 개발, 스테이징, 프로덕션)을 독립적으로 관리할 수 있게 해주는 기능이다. 각 워크스페이스는 자신만의 상태 파일(.tfstate)을 가지므로, 서로 다른 환경의 리소스가 섞이지 않고 관리된다.

### 주요 명령어
* terraform workspace list
  * 현재 존재하는 모든 워크스페이스의 목록을 보여준다.
  * 현재 활성화된 워크스페이스는 이름 앞에 별표(*)가 표시된다.
* terraform workspace new [이름]
  * 새로운 워크스페이스를 생성한다.
  * 생성과 동시에 해당 워크스페이스로 전환된다.
  * *예시:* terraform workspace new production *→ 'production'이라는 새 워크스페이스를 만든다.*
* terraform workspace select [이름]
  * 지정한 이름의 워크스페이스로 전환한다.
  * 이 명령어를 실행한 후 terraform plan이나 apply를 실행하면 해당 워크스페이스의 상태 파일을 기준으로 작업이 수행된다.
  * *예시:* terraform workspace select staging *→ 'staging' 워크스페이스로 작업 환경을 변경한다.*
* terraform workspace show
  * 현재 활성화되어 있는 워크스페이스의 이름을 보여준다.
* terraform workspace delete [이름]
  * 지정한 이름의 워크스페이스를 삭제한다.
  * **주의**: 워크스페이스에 관리되는 리소스가 남아있는 경우 삭제할 수 없다. 먼저 terraform destroy를 실행해야 한다.
  * default 워크스페이스는 삭제할 수 없다.

  ## import block
import 블록은 Terraform v1.5부터 도입된 기능으로, 이미 수동으로 생성된 인프라 자원을 Terraform 코드와 상태 파일 안으로 가져오기 위해 사용하는 선언적(declarative) 방식이다.

기존의 terraform import 명령어와 달리, import 블록은 코드의 일부로 작성되어 plan과 apply 작업 흐름에 통합된다.

```hcl
import {
  to = aws_s3_bucket.my_bucket
  id = "my-unique-terraform-bucket-name"
}
```

* to: Terraform 구성 파일에 정의된 리소스 주소(resource_type.name)를 지정한다.
* id: 실제 클라우드에 존재하는 자원의 고유 ID(S3 버킷의 경우 버킷 이름)를 지정한다.

⠀**주요 특징**
* **계획(Plan)에 통합**: terraform plan을 실행하면, Terraform이 import 블록을 인식하고 해당 자원을 상태 파일로 가져오는 계획을 보여준다. 이를 통해 실제 apply 전에 어떤 자원이 어떻게 import될지 미리 검토할 수 있다.
* **코드 자동 생성 (v1.7 이상)**: terraform plan -generate-config-out=generated.tf 와 같이 옵션을 사용하면, import 하려는 자원의 구성 코드를 자동으로 생성해준다.
* **선언적 관리**: 가져오려는 대상이 코드에 명시적으로 남아있어, 어떤 자원이 외부에서 관리 상태로 전환되었는지 추적하기 용이하다.

## moved block
Terraform 코드의 리소스 주소를 변경(예: 이름 변경, 모듈로 이동)할 때, Terraform이 해당 리소스를 파괴하고 다시 생성하는 대신, 기존 리소스의 상태를 새로운 주소로 안전하게 이전하기 위해 사용하는 선언적 방식이다.

```hcl
resource "aws_instance" "app_server" {
  # ... configuration for the server
}

moved {
  from = aws_instance.web_server
  to   = aws_instance.app_server
}
```
* from: 변경하기 전의 예전 리소스 주소를 지정한다. 이 주소는 더 이상 구성 파일에 존재하지 않아야 한다.
* to: 리소스의 새로운 주소를 지정한다. 이 주소는 현재 구성 파일에 정의되어 있어야 한다.
* **주요 특징**
  * **계획(Plan)에 통합**: terraform plan을 실행하면, Terraform은 moved 블록을 인식하고 상태 파일 내에서 리소스의 주소를 변경하는 계획(~ rename)을 보여준다. 이를 통해 실제 인프라의 변경 없이 상태만 이전되는 것을 확인할 수 있다.
  * **안전한 리팩토링**: 리소스 이름 변경, 리소스를 모듈 안으로 또는 밖으로 이동, 모듈 이름 변경 등 다양한 코드 리팩토링 작업을 할 때, 다운타임 없이 안전하게 상태를 이전할 수 있다.
  * **버전 관리**: 리팩토링 이력이 코드(moved 블록)에 명시적으로 남아있기 때문에, 버전 관리 시스템(Git 등)을 통해 변경 사항을 추적하고 팀원들과 리뷰하기 용이하다.

## required_providers와 provider 블록
### required_providers
terraform 블록 내부에 작성하며, terraform init 시 사용할 프로바이더의 소스(source)와 버전(version)을 정의하는 역할을 함.
* **주요 역할**
  * **의존성 선언**: 프로젝트에 필요한 프로바이더 플러그인을 명시적으로 지정.
  * **버전 고정**: version 속성으로 팀 전체가 동일한 버전을 사용하도록 보장하여 버전 충돌을 방지.
```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}
```

### provider
init을 통해 설치된 프로바이더를 실제 클라우드 플랫폼에 연결하기 위해 인증 정보나 리전(region) 등을 구성하는 역할을 함. plan, apply 시 사용됨.
* **주요 역할**
  * **인증 정보 설정**: 클라우드 API에 접근하기 위한 자격 증명을 구성.
  * **기본값 구성**: 리전, 프로젝트 ID 등 리소스에 공통으로 적용될 기본값을 지정.
```hcl
provider "aws" {
  region = "ap-northeast-2"
}
```

-> required_providers는 terraform 블록 안에 위치하며 어떤 버전의 프로바이더를 사용할지에 대한 내용
-> provider 블록은 설치된 프로바이더를 사용하기 위해 인증정보나 리전등 요구사항을 구성하는 역할