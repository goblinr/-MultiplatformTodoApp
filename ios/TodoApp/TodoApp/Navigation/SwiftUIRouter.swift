//
//  SwiftUIRouter.swift
//  TodoApp
//
//  Created by Roman Romanov on 11.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

final class SwiftUIRouter: Router, ObservableObject {
    
    @Environment(\.presentationMode) private var presentation
    public var screen: Screen? = nil
    
    func back() {
        presentation.wrappedValue.dismiss()
        self.screen = nil
        objectWillChange.send()
    }
    
    func forward(route: Route) {
        self.screen = route.screen
        objectWillChange.send()
    }
    
    func replace(route: Route) {
        if presentation.wrappedValue.isPresented {
            presentation.wrappedValue.dismiss()
        }
        self.screen = route.screen
        objectWillChange.send()
    }
}
