## rebase

특정 브랜치 커밋들의 베이스를 다른 브랜치 커밋으로 만든다.

```bash
$ git checkout [베이스를 바꾸고 싶은 브랜치 이름]
$ git rebase [베이스로 두고 싶은 브랜치 이름]
$ git merge [베이스를 바꾸고 싶은 브랜치 이름]
```

보통 베이스를 바꾸고 싶은 브랜치 이름 = feature/somefeature 이런식을 추가 기능을 의미하게 되고,
베이스로 두고 싶은 브랜치 이름은 develop 브랜치나 master 브랜치가 된다.

그냥 merge 하는 것과 rebase and merge의 다른 점은 그냥 merge는 feature 브랜치의 커밋들이 상세하게
나오지 않고 하나로 묶여서 master에 커밋 이력이 남게 된다. 이를 스쿼시 커밋이라고 한다.

rebase and merge 방식을 사용하면 feature 브랜치의 상세한 내역들이 전부 master의 브랜치에 남게 되고
이 후에 master에서 이력을 깔끔하게 확인하기가 편하다.
