### Backend와 같은 폴더를 이용하여 깃허브에 푸시 할 때

깃허브에 백엔드와 리액트를 CRA로 전부 생성하면 리액트 폴더에 깃이 겹쳐서 안열리는 경우가 있는데 그런 경우엔 서버를 먼저 push하고 CRA로 리액트 앱을 생성한다음

```shell
git rm --cached <folder_name>
```

로 CRA내 깃을 삭제해주고

```shell
 git add <REACT_APP_NAME>/
```

로 추가해준다. 끝에 / 를 붙여서 추가하면 리액트 앱폴더가 디렉토리로 취급받아서 들어가기 때문에 하나의 깃에서 백과 프론트를 모두 관리할 수 있다.

---

### gitignore가 제대로 작동하지 않을 때

캐시를 삭제하고 status를 통해 변화를 확인한 후 다시 푸시해준다.

```shell
$ git rm -r --cached .
$ git add .
$ git status
$ git commit -m "gitignore"
$ git push -u origin <YOUR_BRANCH>
```
