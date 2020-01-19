//
//  SchedulersAssembly.swift
//  TodoApp
//
//  Created by Roman Romanov on 19.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import EasyDi
import Interaction

class SchedulersAssembly: Assembly {
    
    var schedulers: Schedulers {
        return define(scope: .lazySingleton, init: IosSchedulers())
    }
}
