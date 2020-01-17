//
//  TodoData.swift
//  TodoApp
//
//  Created by Roman Romanov on 13.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

struct EnviromentTaskRepositoryKey : EnvironmentKey {
    static var defaultValue: TaskRepository = NetworkTaskRepository()
}

struct EnviromentSchedulersKey : EnvironmentKey {
    static var defaultValue: Schedulers = IosSchedulers()
}

extension EnvironmentValues {
    var taskRepository: TaskRepository {
        get {
            return self[EnviromentTaskRepositoryKey.self]
        }
        set {
            self[EnviromentTaskRepositoryKey.self] = newValue
        }
    }
    
    var schedulers: Schedulers {
        get {
            return self[EnviromentSchedulersKey.self]
        }
        set {
            self[EnviromentSchedulersKey.self] = newValue
        }
    }
}

#if DEBUG
final class MockTaskRepository : AbstractTaskRepository {
    
    override func blockingTasks(success: @escaping ([Task]) -> Void, error: @escaping (KotlinThrowable) -> Void) {
        success(
            FreezeKt.freezeTaskList(list: [
                Task(id: "1", title: "One", description: "First task", status: TaskStatus.pending),
                Task(id: "2", title: "Two", description: "Second task", status: TaskStatus.done)
            ])
        )
    }
    
    override func blockingSwitchTask(id: String, success: @escaping () -> Void, error: @escaping (KotlinThrowable) -> Void) {
        
    }
    
    override func blockingArchiveTask(success: @escaping () -> Void, error: @escaping (KotlinThrowable) -> Void) {
        
    }
    
    override func blockingUnarchiveTask(success: @escaping () -> Void, error: @escaping (KotlinThrowable) -> Void) {
        
    }
    
    override func blockingCreateTask(title: String, description: String, success: @escaping (Task) -> Void, error: @escaping (KotlinThrowable) -> Void) {
        
    }
}
#endif
