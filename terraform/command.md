## Basic Command
```bash
terraform init
```
Terraform 구성 파일을 사용하여 작업을 시작하기 위해 작업 디렉토리를 초기화
1. 백엔드 초기화
- 인프라의 상태를 추적하기 위해 사용하는 상태파일(terraform.tfstate)을 사용
- 별도 설정 없으면 상태 파일을 로컬에 저장
- 여러 사용자가 협업하거나 상태 파일을 안전하게 보관하기 위해 S3와 같은 원격 저장소를 백엔드로 지정할 수 있음
- init은 이 원격 백엔드와 연결하고 인증하는 작업도 수행

2. 프로바이더 플러그인 다운로드
- 파일의 provider "aws" {} 를 확인
- 클라우드 서비스 프로바이더와 통신, 해당 프로바이더의 공식 레지스트리에서 필요한 플러그인 다운로드
- .terraform.lock.hcl 파일으 통해 팀원 모두가 동일한 버전의 플러그인을 사용하도록 고정

3. 모듈 설치
- 재사용 가능한 인프라 구성단위인 모듈을 사용하는 경우 모듈을 다운로드하여 설치

`terraform init`의 사용 시점은
- 새로운 테라폼 프로젝트를 시작하거나
- Git에서 새 프로젝트를 클론하거나
- 기존 코드에 새로운 프로바이더 혹은 모듈을 추가했을 때 
- 백엔드 구성을 변경했을 때

```bash
terraform apply
```
구성 파일(.tf)을 기준으로 인프라의 생성 및 변경을 실행

1. 실행 계획 수립
- 리소스가 존재하는지 확인
- 코드와 상태 파일(terraform.tfstate)을 비교하여 변경점 파악
- 생성(+), 수정(~), 삭제(-)될 리소스 목록을 사용자에게 표시

2. 사용자 승인
- 실행 계획을 실제 적용하기 전, 사용자에게 yes 입력을 요구
- 의도치 않은 변경을 방지하는 안전장치 역할

3. 인프라 적용
- 승인 시, 프로바이더 API를 호출하여 실제 리소스를 생성하거나 변경
- 계획된 작업을 순서에 따라 실행

4. 상태 파일 갱신
- 실행 완료 후, 변경된 인프라의 최종 상태를 terraform.tfstate 파일에 기록
- 코드와 실제 인프라 간의 상태를 동기화

```bash
terraform destroy
```
Terraform으로 관리되는 모든 인프라 리소스를 제거

1. 파괴 계획 수립
- 삭제(- destroy)될 모든 리소스의 목록을 사용자에게 표시

2. 사용자 승인
- 모든 리소스를 영구적으로 삭제하기 전, 사용자에게 yes 입력을 요구
- 실수로 인한 전체 인프라 삭제를 방지하는 최종 안전장치

3. 인프라 삭제 실행
- 사용자 승인 시, 프로바이더 API를 호출하여 리소스 삭제 진행
- 일반적으로 리소스 간의 의존성을 고려하여 생성의 역순으로 삭제

4. 상태 파일 갱신
- 모든 리소스 삭제가 완료되면, terraform.tfstate 파일에서 해당 리소스 정보를 모두 제거
- 상태 파일을 비워 관리할 리소스가 없음을 기록

```bash
terraform state pull
```
테라폼과 현재 연결된 백엔드에서 state 정보를 가져온다.

```bash
terraform init -migrate-state
```
테라폼과 이전에 연결된 백엔드에서 state 정보를 가져와서 현재 연결된 백엔드에 이전한다.

```bash
terraform force-unlock {LOCK_ID}
```
테라폼의 잠금을 활성화 했을 때(Dynamodb 등..) 해당 잠금을 해제한다.

## alias
- alias를 통해 같은 타입(aws, azure 등..)의 프로바이더를 한 테라폼 내에서 여러개를 사용할 수 있다.
```bash
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

# 기본 프로바이더: us-east-1
provider "aws" {
  region = "us-east-1"
}

# 별명("west") 프로바이더: us-west-1
provider "aws" {
  alias  = "west"
  region = "us-west-1"
}

# 1. us-east-1 리전에 S3 버킷 생성
# provider 인수가 없으므로 '기본' 프로바이더를 사용합니다.
resource "aws_s3_bucket" "bucket_east" {
  bucket = "my-unique-bucket-for-east-region-12345" # 버킷 이름은 전역적으로 고유해야 합니다.
  
  tags = {
    Name = "My bucket in us-east-1"
  }
}

# 2. us-west-1 리전에 S3 버킷 생성
# provider = aws.west 를 지정하여 'west' 별명을 가진 프로바이더를 사용합니다.
resource "aws_s3_bucket" "bucket_west" {
  provider = aws.west # 이 부분이 핵심입니다!
  
  bucket = "my-unique-bucket-for-west-region-67890" # 버킷 이름은 전역적으로 고유해야 합니다.

  tags = {
    Name = "My bucket in us-west-1"
  }
}
```

## -out option
```bash
terraform plan -out={name}
terraform apply {name}
```
- 현재 plan을 파일로 저장해두었다가 이 후에 적용할 수 있다.

## login
```bash
terraform login
```
- HCP Terraform이나 Terraform Enterprise처럼 HashiCorp가 직접 제공하는 서비스형 백엔드에 접속할 때만 사용

## state rm
```bash
module, type, name은 tfstate 파일을 따름
terraform state rm '{module}.{type}.{name}'
```
- 해당 리소스를 state 파일에서만 제거한다. 이미 사라진 리소스를 state의 추적 목록에서 제거 할 때 사용


terraform apply -refresh-only
```bash
terraform apply -refresh-only
```
- 현재 tf 코드들과 상관없이 실제 클라우드 리소스의 상태를 확인하여 테라폼의 상태파일만 갱신한다.
- 코드와 일치시키기 위한 리소스 변경 작업을 발생하지 않는다.
- 코드에 변경점이 없는지 확인하고 싶을 때, 환경의 실제 변동사항만 최신상태로 기록하고 싶을 때, 불필요한 변경, 배포 없이 상태만 동기화해야할 때 주로 사용

## terraform apply -replace
```bash
terraform apply -replace="aws_instance.example"
```
- 특정 리소스를 강제로 삭제하고 재생성할 때 사용
- 상태파일에는 정상적이나 실제 리소스가 오작동하는 경우
- 특정 속성을 바꾸기 위해 재설치가 필요할 때

## terraform console
- 터미널에서 실시간으로 표현식, 함수, 변수를 테스트할 수 있는 대화형 명령어 환경 실행
- 내장 함수 테스트, 변수값 확인, 리소스 및 데이터의 속성 조회, 복잡한 표현식 테스트 등에 사용

## parallelism 옵션
```bash
terraform apply -parallelism=20
```
- 테라폼은 기본적으로 최대 10개의 리소스를 동시에 병렬적으로 생성하거나 수정함
- 서로 의존성이 없는 경우에 기다릴 필요가 없으므로 효율적으로 생성하기 위함
- 디폴트로 10개를 생성하나 parallelism 옵션을 통해 수정 가능

## terraform state 관련
| **명령어** | **역할** |
|---|---|
| terraform state list | 상태 파일에 기록된 모든 리소스 목록을 출력한다. |
| terraform state show <리소스> | 특정 리소스의 상태 세부 정보를 보여준다. |
| terraform state pull | 상태 파일 내용을 JSON 형식으로 출력한다. |
| terraform state rm <리소스> | 상태 파일에서 특정 리소스를 제거한다. 실제 리소스는 삭제되지 않는다. |
| terraform state mv <원본> <대상> | 상태 파일 내 리소스를 다른 이름이나 위치로 이동한다. |
| terraform state push | 로컬 상태 파일 변경 내용을 원격 저장소에 반영한다. |
| terraform refresh | 실제 인프라 상태와 상태 파일을 동기화하여 최신으로 갱신한다. |
## terraform get
### terraform get
* 선언된 모듈들 중에서 아직 다운로드되지 않은 모듈을 찾아서 받아오는 명령어
* 모듈만 별도로 다시 받고 싶을 때 사용

### terraform init
* 작업 디렉터리를 초기화하는 명령어
* 필요한 플러그인과 프로바이더를 설치
* 백엔드 설정을 구성
* 모듈 전체를 다운로드까지 수행

### 주요 차이점
* terraform init은 초기화 작업 전체를 담당하며, 모듈 다운로드까지 포함
* terraform get은 모듈만 별도로 다운로드하거나 업데이트할 때 사용
* 보통 처음 작업 시작하거나 백엔드 변경 시 init 사용
* 모듈 소스가 변경되어 모듈만 다시 받고 싶을 때 get 사용한다

## terraform import
terraform import 명령어는 기존에 이미 생성된 인프라 리소스를 테라폼 상태 파일(tfstate)로 가져오는 명령어다.
### 핵심 내용
* 이미 존재하는 인프라 리소스를 tfstate에 추가하여 테라폼으로 관리할 수 있게 만든다.
* import하려는 리소스에 대한 빈 리소스 블록을 테라폼 코드에 먼저 정의해야 한다.
* 리소스 주소와 실제 리소스 ID를 인자로 받아서 상태 파일에 등록한다.
* import는 상태 파일에만 반영하고, 테라폼 코드(설정 파일)는 자동 생성하지 않는다.
* import 후 **terraform plan**으로 코드와 상태 일치 여부를 반드시 확인하고 조정해야 한다.
* 각 리소스별로 리소스 ID 형식이 다르므로 제공자 문서를 참고해야 한다.

### 예시
```bash
terraform import aws_s3_bucket.my_bucket existing-bucket-name
```

위 명령은 기존 AWS S3 버킷을 **aws_s3_bucket.my_bucket** 리소스로 상태에 등록한다.
이 명령어는 기존 인프라를 테라폼으로 관리할 때 꼭 사용해야 하는 명령어다.

## terraform validate
* 테라폼 구성 파일의 문법과 내부 논리적 일관성을 검사하는 명령어
* 원격 상태나 실제 인프라와는 상관없이 로컬에서 코드 자체의 유효성만 확인
* 주로 작성 중인 테라폼 코드에 문법 오류가 없는지 빠르게 검증하기 위해 사용

## terraform plan
* 현재 상태 파일과 실제 인프라 상태를 기준으로, 테라폼 구성 파일에 정의된 변경 사항을 시뮬레이션하는 명령어
* 어떤 리소스가 생성, 변경, 삭제될지 실행 계획을 미리 보여줌
* 실제로 적용하기 전에 변경 내용을 미리 검토하기 위해 사용

## plan과 validate의 주요 차이점
| **항목** | **terraform validate** | **terraform plan** |
|---|---|---|
| 검사 범위 | 코드 문법 및 논리적 유효성 | 실제 인프라 상태와 비교해 변경 사항 시뮬레이션 |
| 원격 상태 확인 | 하지 않음 | 상태 파일과 원격 리소스 조회 및 비교 수행 |
| 목적 | 문법 오류 및 구성 체계 검증 | 적용할 변경 내용 미리 확인, 위험 및 영향 분석 |
| 인프라 변경 여부 | 없음 | 없음 (변경 내용 예측만 함) |
| 실행 시점 | 코드 작성 초중반, 자주 실행 가능 | 변경 적용 전 반드시 실행해 결과 확인 필요 |

validate는 문법 오류 등 빠른 코드 점검 용도이고, plan은 인프라 변경을 미리 예측하고 검토하는 절차라는 점이 핵심 차이라 할 수 있다

## terraform show
terraform show 명령어는 테라폼 상태 파일(state)이나 실행 계획(plan) 파일의 내용을 사람이 읽기 쉬운 형태로 출력하는 데 사용된다.

### 주요 특징
* 현재 상태 파일이나 plan 파일에서 리소스 속성 정보 확인
* 리소스가 가진 상세 속성(IP, ID, 설정값 등) 출력
* JSON 형식 출력 가능(**-json** 옵션)하여 자동화 스크립트나 분석 도구와 연동 가능
* 별도 파일을 지정하지 않으면 최신 상태 파일을 기본으로 보여줌

terraform show는 상태 정보나 실행 계획을 시각적으로 확인하거나 검토하는 데 유용하다.

## terraform state rm
```bash
# module, type, name은 tfstate 파일을 따름
terraform state rm '{module}.{type}.{name}'
```
해당 리소스를 state 파일에서만 제거한다. 이미 사라진 리소스를 state의 추적 목록에서 제거 할 때 사용


## terraform fmt
terraform fmt는 Terraform 코드(HCL)를 공식 스타일 가이드에 맞춰 자동으로 정렬해주는 명령어이다. 코드의 기능에는 전혀 영향을 주지 않고, 오직 가독성과 일관성을 높이기 위해 들여쓰기 공백, 줄 맞춤 등을 정리한다.
