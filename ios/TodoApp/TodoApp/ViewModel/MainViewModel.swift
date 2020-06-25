//
//  MainModel.swift
//  TodoApp
//
//  Created by Roman Romanov on 11.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

final class MainViewModel : ObservableObject {
    var screen: Screen = Screen.todoList
    var state: ViewState = TodoListViewModel()
}
