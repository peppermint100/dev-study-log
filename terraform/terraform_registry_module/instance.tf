provider "aws" {
  region     = "ap-northeast-2"
}

resource "aws_instance" "example_server" {
  ami           = "ami-0dd97ebb907cf9366"
  instance_type = "t2.micro"
  subnet_id = module.vpc.public_subnets[0]
  vpc_security_group_ids = [aws_security_group.allow_ssh.id]
}

resource "aws_key_pair" "mykey" {
  key_name = "mykey"
  public_key = "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIPAbK6iG91bObvFieLYaOj2WOmvvE5E7BAxYEc2jni1M peppermint100@PepperMac.local"
}