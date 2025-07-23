import Foundation

let queue = [1,2,3,4,5,6,7,8,9,10]

let serialQueue: DispatchQueue = DispatchQueue(label: "serial")

for i in 0..<10 {
    serialQueue.async {
        print("serial \(i): ", queue[i])
    }
}

let concurrentQueue: DispatchQueue = DispatchQueue(label: "concurrent", attributes: .concurrent)

print("======concurrent=======")

for i in 0..<10 {
    concurrentQueue.async {
        print("concurrent \(i): ", queue[i])
    }
}

let syncQueue = DispatchQueue(label: "sync")

syncQueue.sync {
    print("sync 1")
}

syncQueue.sync {
    print("sync 2")
}

syncQueue.sync {
    print("sync 3")
}

print("Hello Sync")

let asyncQueue = DispatchQueue(label: "async")

print("Hello Async")

asyncQueue.sync {
    print("async 1")
}

asyncQueue.sync {
    print("async 2")
}

asyncQueue.sync {
    print("async 3")
}

