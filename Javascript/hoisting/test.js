sayHello();
sayHello2(); // ERROR!!

function sayHello() {
  // [Hoisting] 함수선언문
  console.log("hello1");
}

sayHello2 = function() {
  console.log("hello2");
};
