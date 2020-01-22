//
//  SwiftUIRouter.swift
//  TodoApp
//
//  Created by Roman Romanov on 11.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

enum Command {
    case back
    case forward(route: Route)
    case replace(route: Route)
}

final class Navigator : ObservableObject {
    
    private var data: Array<Any?> = []
    
    public func lastData() -> Any? {
        return data.last ?? nil
    }
    
    internal func executeCommands(commands: Array<Command>) {
        commands.forEach() { command in
            execute(command)
        }
    }
    
    internal func execute(_ command: Command) {
        switch command {
        case .back:
            if !data.isEmpty {
                data.removeLast()
            }
        case .forward(let route):
            data.append(route.data)
        case .replace(let route):
            if (!data.isEmpty) {
                data[data.count - 1] = route.data
            } else {
                data.append(route.data)
            }
        }
        objectWillChange.send()
    }
}

protocol SwiftRouter : Router {
    
    func attachNavigator(navigator: Navigator)
    
    func detachNavigator()
}

final class SwiftUIRouter: SwiftRouter {
    
    private var commands: Array<Command> = []
    private var navigator: Navigator? = nil
    
    func attachNavigator(navigator: Navigator) {
        self.navigator = navigator
        navigator.executeCommands(commands: commands)
        commands = []
    }
    
    func detachNavigator() {
        self.navigator = nil
    }
    
    func back() {
        if navigator != nil {
            navigator?.execute(Command.back)
        } else {
            self.commands.append(Command.back)
        }
    }
    
    func forward(route: Route) {
        let command = Command.forward(route: route)
        if navigator != nil {
            navigator?.execute(command)
        } else {
            self.commands.append(command)
        }
    }
    
    func replace(route: Route) {
        let command = Command.replace(route: route)
        if navigator != nil {
            navigator?.execute(command)
        } else {
            self.commands.append(command)
        }
    }
}
