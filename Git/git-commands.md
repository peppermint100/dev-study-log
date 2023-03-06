# 깃허브 기본 커맨드

**[출처](lainyzine.com/ko/article/summary-of-how-to-use-git-for-source-code-management/#git-설정과-기초-편)**

# 깃 설정

---

## 깃 생성

```
1. 깃에 올릴 로컬 파일 시스템 생성 및 접근
2. github.com/new 에 접속
3. 웹 콘솔에서 깃허브 정보 작성
4. 콘솔에 있는 대로 명령어 순서대로 로컬 파일 시스템 터미널에 작성
```

## 사용자 이름, 이메일 설정

```bash
$ git config --global user.name "Your Name"
$ git config --global user.email you@example.com
# global 옵션을 주면 파일 시스템 전체에서 기본적으로 저 설정을 사용하며
# 깃을 사용하는 폴더마다 다른 이름, 이메일을 줄 수도 있다.
# 그 경우는 global 옵션을 주지 않으면 되고, global 옵션의 정보보다 우선 적용된다.

$ cat ~/.gitconfig # 현재 깃 유저 설정 확인

# 전역 설정을 삭제
$ git config --global --unset user.name
$ git config --global --unset user.email

# 개별 저장소의 설정을 삭제
$ git config --unset user.name
$ git config --unset user.email
```

## Git 저장소와 Github 사용자가 연결되는 원리

```bash
Github는 Git 커밋의 이메일 정보를 사용하여 매칭한다. Github에서는 Primary 이메일을
등록할 수 있으며 메일 설정 후 Github 저장소에 Push하면 해당 이메일을 사용하는 Github
사용자로 자동 연결한다.

단 email은 작성자 확인용이고 증명, 인증 로직은 없다. 이메일만 바꿔서 커밋하면 sourcetree든
bitbucket이든 github든 Authorization 과정을 거치게 된다.
```

## 깃허브 파일의 상태

![Untitled](%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%80%E1%85%B2%209f6a0800a2bb4934b832e77cb132d42c/Untitled.png)

```jsx
1. working directory: 로컬에서 코드를 작성한 상태
2. stage: git add로 파일을 올린 상태
3. history: git commt으로 파일을 커밋하여 git에 남는 히스토리로 작성한 상태
```

# 깃 허브 기본 커맨드

---

## add

파일을 stage에 올린다.

```bash
git add .
git add [파일이름]
```

## commit

파일을 history에 올린다.

```bash
git commit => 커밋 메시지를 적을 수 있는 vi 에디터가 열림
git commit -m [커밋 메시지]
```

깃의 저장과정은 파일 변경 → stage 추가(add) → 커밋으로 나뉘어진다. 커밋에서는 HEAD가 최신 커밋을 가리키게 된다. 커밋을 하면 새로운 커밋 객체를 만들어 생성한 후 그 커밋객체가 바로 이전 커밋을 가리키게 한 후 최신 커밋 객체를 HEAD로 지정한다.

## commit —amend

직전 커밋에 현재 stage 변화를 덮어쓴다.

```bash
git commit --amend
```

실행하면 이전 커밋 메시지를 포함한 vi 에디터가 열리면 커밋을 덮어 씌울 수 있다.

## show

커밋의 상태를 확인한다.

```bash
git show // 전체 상태 확인
git show [커밋 ID] // 해당 ID에 해당하는 커밋 정보 확인
```

## diff

1개 파일의 Working Directory와 Stage의 차이를 확인한다.

```bash
git diff [path/to/file]
```

모든 파일의 변화를 추적하는 방법은 없으며 모든 파일 변화가 알고 싶다면

```bash
git diff --name-only --cached
```

이 명령어를 통해 쉘스크립트로 for문을 돌려서 확인해야 한다.

```bash
for file in $(git diff --name-only --cached); do
  git diff $file
done
```

## rm —cached

스테이지에 올라간(add로 추가된) 파일들을 다시 working directory로 내린다

```bash
$ git rm --cached [파일이름 혹은 경로]
```

## reset

커밋 이후 스테이지에 올라간 파일들을 다시 working directory로 내린다

```bash
$ git reset HEAD
$ git reset HEAD [파일이름]
```

## reset —hard

깃 커밋을 취소합니다. 

```bash
$ git reset --hard [커밋ID]
```

해당 커밋 ID로 깃 커밋을 되돌립니다. 변경사항도 모두 돌아가고 커밋 자체가 사라집니다.

## revert

커밋을 취소하는 커밋을 추가합니다.

reset을 통해 커밋을 지우면 리모트 브랜치와 차이가 날 수 있습니다. 그런 경우를 방지하기 위해 revert를 사용합니다.

```bash
$ git revert [커밋ID]
```

## restore

스테이지에 올라간 파일을 다시 working directory로 내린다. git 2.23.0 부터 사용가능

reset, rm —cached는 파일이름을 지정해주지 않으면 전체로 자동 적용되지만 restore는 파일이름을

지정해주어야 한다.

```bash
git restore --staged [파일이름]
```

## log

깃 커밋의 로그를 본다

```bash
$ git log
$ git log --oneline # 한줄로 간단하게 표시
```

# 브랜치

---

## 머지된 브랜치와 머지되지 않은 브랜치

```bash
$ git branch --merged
$ git branch --no-merged
```

## 브랜치 삭제하기

```bash
$ git branch -d [브랜치 이름]
```

단 브랜치중 머지되지 않은 브랜치는 삭제할 수 없으므로 머지를 해줘야 한다. 머지 되지 않은 브랜치는 위에 머지된 브랜치와 머지되지 않은 브랜치 명령어에서 확인할 수 있다.

하지만 이것과 상관없이 강제로 브랜치를 삭제하고 싶다면

```bash
$ git branch -D [브랜치 이름]
혹은
$ git branch --delete --force [브랜치 이름]
```
