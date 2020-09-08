## Problem
You are given a data structure of employee information, which includes the employee's unique id, their importance value and their direct subordinates' id.

For example, employee 1 is the leader of employee 2, and employee 2 is the leader of employee 3. They have importance value 15, 10 and 5, respectively. Then employee 1 has a data structure like [1, 15, [2]], and employee 2 has [2, 10, [3]], and employee 3 has [3, 5, []]. Note that although employee 3 is also a subordinate of employee 1, the relationship is not direct.

Now given the employee information of a company, and an employee id, you need to return the total importance value of this employee and all their subordinates.

## Approach
1. 시작 점을 찾는다.
2. 시작 점을 큐에 넣고 큐에서 하나 씩 꺼내가며 importance를 더한다.

```javascript
 * function Employee(id, importance, subordinates) {
 *     this.id = id;
 *     this.importance = importance;
 *     this.subordinates = subordinates;
 * }
```

릿코드에서 subordinates가 상위 프로토타입인 Employ를 다시 체이닝하지 않아서
employee를 계속 순환하느라 코드가 길어졌다.