//
//  ContentView.swift
//  TodoApp
//
//  Created by Roman Romanov on 09.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction
import EasyDi

struct ContentView: View {
    
    @ObservedObject var model: MainViewModel
    
    var body: some View {
        NavigationView {
            VStack {
                if model.screen == Screen.todoList {
                    TodoListView(
                        model: model.state as! TodoListViewModel
                    )
                } else if model.screen == Screen.createTask {
                    CreateTaskView(
                        model: model.state as! CreateTaskViewModel
                    )
                }
            }
        }
    }
}

#if DEBUG
struct ContentView_Previews: PreviewProvider {
    static var listState: MainViewModel {
        let model = MainViewModel()
        model.screen = Screen.todoList
        model.state = TodoListViewModel()
        return model
    }
    
    static var previews: some View {
        Group {
            ContentView(
                model: listState
            ).previewDisplayName("Light")
            
            ContentView(
                model: listState
            ).environment(\.colorScheme, .dark)
            .previewDisplayName("Dark")
        }
    }
}
#endif
