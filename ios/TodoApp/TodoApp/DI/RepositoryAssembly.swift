//
//  RepositoryAssembly.swift
//  TodoApp
//
//  Created by Roman Romanov on 19.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import EasyDi
import Interaction

class RepositoryAssembly: Assembly {
    
    var taskRepository: TaskRepository {
        return define(scope: .lazySingleton, init: NetworkTaskRepository())
    }
}
