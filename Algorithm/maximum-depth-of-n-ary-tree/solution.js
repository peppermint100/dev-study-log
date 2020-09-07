var maxDepth = function(root) {
    if(!root) return 0;
    
    const queue = [root]
    let height = 0

    while(queue.length){
        const size = queue.length
        for(let i = 0; i < size; i++){
            const node = queue.shift()
            for(let j = 0; j < node.children.length; j++){
                queue.push(node.children[j])
            }    
        }
        height++
    }

    return height
} 