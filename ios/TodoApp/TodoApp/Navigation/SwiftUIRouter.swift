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
    
    @Environment(\.presentationMode) private var presentation
    public var screen: Screen? = nil
    
    func executeCommands(commands: Array<Command>) {
        commands.forEach() { command in
            execute(command)
        }
    }
    
    func execute(_ command: Command) {
        switch command {
        case .back:
            if presentation.wrappedValue.isPresented {
                presentation.wrappedValue.dismiss()
            }
            self.screen = nil
            objectWillChange.send()
        case .forward(let route):
            self.screen = route.screen
            objectWillChange.send()
        case .replace(let route):
            if presentation.wrappedValue.isPresented {
                presentation.wrappedValue.dismiss()
            }
            self.screen = route.screen
            objectWillChange.send()
        }
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
