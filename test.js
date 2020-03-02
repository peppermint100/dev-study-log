function outFunc(outVar) {
  return function inFunc(inVar) {
    console.log(" outVar : ", outVar);
    console.log(" inVar: ", inVar);
  };
}

const out = outFunc("outVar");
out("inVar");
