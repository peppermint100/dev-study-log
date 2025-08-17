provider "aws" {
  region     = "ap-northeast-2"
}

resource "aws_instance" "example_server" {
  ami           = "ami-0dd97ebb907cf9366"
  instance_type = "t2.micro"
  subnet_id = module.vpc.public_subnets[0]
}