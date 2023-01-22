# ECS With Terraform

- [Docker](https://www.notion.so/Docker-66e103de67714ed3ad1abe78a4b7c2cc) 와 연관

## ECS With Terraform #1

## 서론

회사에 새로운 서버 기술을 도입해보려고 한다. 사실 이전에도 이것저것 도입해보려 했지만 다른 업무에 치이는 일이 있었고 스스로도 동기부여에 실패하여 도입에 실패한 기술들이 좀 있다(webflux…).

> 하지만 이번엔 다르다. 2023년 1월이니까
> 

지금 당장은 아니지만 새로운 서버 어플리케이션을 출시해야 할 일이 생겼고, 이 때 원하는 기술을 최대한 적용하겠다고 회사에 선포했다.

하나는 Terraform이고 하나는 Docker이다. Terraform은 최근에 열심히 공부중이다. /l

## Elastic Container Service

## ECS?

Elastic Container Service는 컨테이너 오케이스트레이션 툴이다. 확장성이 뛰어나고 빠르며 많이 사용하는 쿠버네티스보다 훨씬 쉽다고 한다. 

또 AWS 생태계 내에 있으므로 VPC, Security Group 등으로 추상적인 네트워크 망을 만들 수 있으며 AWS 다른 서비스들과도 매끄럽게 연결이 가능하다. 

### 연결 관련 서비스

IAM: AWS 리소스에 대한 접근을 안전하게 제어할 수 있다.

CloudWatch: ECS에 대한 실시간 모니터링이 가능하며 컨테이너 내부에 로그가 저장되지 않으므로 인스턴스 내의 공간을 아낄 수 있다.

EC2 Auto Scaling: 클러스터 내의 컨테이너 인스턴스 크기를 조정할 수 있다. 

Elastic Load Blancing: 로드 밸런서를 통해 어플리케이션의 수신 트래픽을 여러 태스크에 분산할 수 있다.

Container Registry: AWS 내에 이미지를 저장하고 관리할 수 있다.

## 컨테이너 관리 방식

ECS 클러스터에서는 2가지 옵션을 통해 컨테이너를 관리한다.

Fargate와 EC2 방식이 있으며,

**Fargate**는 서버리스 형식으로 서버 관리, 용량 계획 처리, 보안을 위한 컨테이너 워크로드 격리 등을 AWS가 알아서 해준다. 인프라를 관리할 필요가 없으며 다음과 같은 상황에 적절하다.

- 낮은 오버헤드를 위해 최적화해야 하는 대규모 워크로드
- 가끔 버스트가 발생하는 소규모 워크로드
- 작은 워크로드
- 배치 워크로드

참고로 인프라를 전부 AWS에서 관리해주고 버스트 발생시에도 알아서 처리해주므로 EC2보다는 일반적으로 비싸다고 한다.

**EC2**는 일반적으로 우리가 아는 EC2 인스턴스 위에 컨테이너를 올려서 사용하는 방식이다. EC2 방식은 아래와 같은 방식에 적절하다.

- 지속적으로 높은 CPU 코어 및 메모리 사용량이 필요한 워크로드
- 가격에 최적화되어야 하는 대규모 워크로드
- 애플리케이션이 영구 스토리지에 액세스해야 함
- 인프라를 직접 관리해야 함

EC2 방식은 일반적인 EC2와 가격정책이 같다고 한다. 일반적인 경우에 Fargate보다 가격이 효율적이라고 하며 가장 좋은 점은 아무래도 컨테이너를 실행하는 시스템 내부를 직접 관리할 수 있다는 점이 좋아 보인다.

> ECS, EKS는 당연히 Fargate를 사용할 줄 알았는데, ECS를 사용하는 다른 회사들이 거의 EC2를 활용하는 것을 보고 놀랐다. 그 때는 이해하지 못했지만 가격과 인프라를 커스텀하는 점을 생각하면 EC2가 더 좋아보인다.
> 

## 요금

> 그래서 얼마냐
> 

도커가 좋은건 현업에서 도커를 활용하지 않으며 뼈저리게 느끼고 있다. 그래서 현 회사에 도커를 도입하려고 열심히 노력하고 있다. 

하지만 가격적인 부분을 생각하지 않을 수 없다. 하지만 ECS는 무려 공짜다. Fargate 사용 요금, EC2 사용 요금을 제외하면 말이다. 즉 컨테이너 오케스트레이션에 대한 비용은 없다는 뜻으로 보인다.

Fargate는 컨테이너화(containerized)된 어플리케이션에서 요청하는 vCPU, 메모리 사용량에 따라 금액이 청구된다. 1분의 최소 요금이 있다. 스팟 인스턴스, Fargate의 Saving Plan을 통해 요금 최적화가 가능하다.

EC2는 EC2 요금제를 확인하면 된다. 이 역시 스팟 인스턴스, Saving plan을 통해 요금 최적화가 가능하다.

[서버리스 컴퓨팅 엔진 - AWS Fargate 요금 - Amazon Web Services](https://aws.amazon.com/ko/fargate/pricing/)

[Amazon EC2 - 크기 조정이 가능한 안전한 컴퓨팅 용량 - Amazon Web Services](https://aws.amazon.com/ko/ec2/pricing/)

## ECS 구성

ECS 클러스터는 작업 또는 서비스의 논리적 그룹이다. 하나의 어플리케이션이 하나의 클러스터 단위를 의미한다. Fargate에서 작업이 실행되면 클러스터 리소스도 Fargate에서 관리한다.

### Task Definitions

공식 문서가 한글화가 되어 있다면 한국어로 읽는 편이지만 AWS 콘솔은 영어로 해두고 사용한다. 스택오버플로우나 한글화가 되지 않은 AWS 문서를 읽을 때 Task Definitions과 같은 AWS 고유명사(?) 는 이해하기가 힘들기 때문이다. 한국어로는 태스크 정의라 되어 있다.

Task Definition은 어플리케이션의 컨테이너들을 보여주는 텍스트 파일이며 JSON 형식이다. 최대 10개의 컨테이너를 작성할 수 있으며 설계도와 같은 역할을 한다.

다양한 파라미터를 통해 컨테이너, 운영체제, 네트워크 포트, 데이터 볼륨등을 정할 수 있다. 

한 어플리케이션이 하나의 Task Definition을 가질 필요는 없다. 실제로도 여러 개의 Task Definitions를 작성하여 어플리케이션을 확장가능하도록 작성하는게 좋다고 한다.

### Task

이 역시 AWS 용어이며 Task는 클러스터 안의 태스크 정의를 인스턴스화 하는 것이다. ECS에서 Task Definitions를 생성 할 때 클러스터에서 실행할 Task 수를 지정할 수 있다. 이를 Fargate나 EC2 인스턴스에 배포하게 된다.

### Service

Service 역시 여러 프로그래밍 세계에서 다양한 뜻이 있지만 ECS만의 서비스가 존재한다. ECS 서비스는 클러스터에서 원하는 수의 태스크를 동시에 실행하고 유지할 수 있게 한다. 태스크가 중지 되면 서비스 스케쥴러가 Task Definitions에 따라 다른 인스턴스를 실행시키며 Task의 수를 유지시킨다.

이 서비스는 Task를 포함하며 Auto Scaling, Load Balancing을 관리한다.

> 여기까지 읽어보니 Task가 Kube의 pod처럼 하나의 컨테이너이며 Service를 통해 오케스트레이션 하는게 느껴진다.
> 

### Container Agent

컨테이너 에이전트는 ECS 클러스터 내의 각 컨테이너 인스턴스에서 실행된다. 현재 인스턴스의 리소스 사용 정보를 ECS에 전송하여 태스크를 관리하도록 도와준다.

![Untitled](https://velog.velcdn.com/images/peppermint100/post/0fc35eed-f96b-4aae-99b2-f8c91b86b0a7/image.png) 
ECS 에이전트가 인스턴스 내부의 상황을 읽고 태스크를 관리해준다.

### Fargate Architecture

ECS 자체는 여러 Availablity Zone에서 실행되는 컨테이너들을 관리해준다. ECS 클러스터를 VPC 내에 생성하고 클러스터 내에서 실행할 컨테이너 이미지를 Task Definitions를 통해 정의하면 된다. 이를 통해 Task를 실행하거나 Service를 생성할 수 있다.

![Untitled](https://velog.velcdn.com/images/peppermint100/post/ae895687-7846-48cd-a23a-538d74d44288/image.png)

대략 위와 같이 이루어져 있는데, 보면 Registry부터 이미지를 가져와서 태스크가 생성되고 이를 Elastic Network Interface를 통해 내보내는 것 같다.

또 이 태스크들은 Task Definitions와 Service Description에 의해 관리된다. 공식 문서를 볼 땐 몰랐지만 이 두 개가 Service에 속하는 것으로 보인다.

### 출처

[https://1mini2.tistory.com/123](https://1mini2.tistory.com/123)

[https://docs.aws.amazon.com/ko_kr/AmazonECS/latest/developerguide/Welcome.html](https://docs.aws.amazon.com/ko_kr/AmazonECS/latest/developerguide/Welcome.html)
[https://tech.cloud.nongshim.co.kr/2021/08/30/소개-amazon-ecs란/](https://tech.cloud.nongshim.co.kr/2021/08/30/%EC%86%8C%EA%B0%9C-amazon-ecs%EB%9E%80/)