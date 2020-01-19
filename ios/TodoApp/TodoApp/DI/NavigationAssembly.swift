//
//  NavigationAssembly.swift
//  TodoApp
//
//  Created by Roman Romanov on 19.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import EasyDi
import Interaction

class NavigationAssembly: Assembly {
    
    var router: SwiftRouter {
        return define(scope: .lazySingleton, init: SwiftUIRouter())
    }
}
