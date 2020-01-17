//
//  CreateTaskView.swift
//  TodoApp
//
//  Created by Roman Romanov on 14.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

struct CreateTaskView: View {
    
    let mainStore: () -> MainStore
    
    @ObservedObject private var model = CreateTaskModel()
    
    @SwiftUI.State private var title = ""
    @SwiftUI.State private var description = ""
    
    var body: some View {
        VStack {
            Spacer()
            
            if model.result == nil {
                TextField("Title", text: $title, onEditingChanged: { value in
                    self.model.acceptAction(
                        action: CreateAction.UpdateField(
                            value: self.title,
                            type: FieldType.title
                        )
                    )
                }).padding()
                
                TextField("Description", text: $description, onEditingChanged: { value in
                    self.model.acceptAction(
                        action: CreateAction.UpdateField(
                            value: self.description,
                            type: FieldType.description_
                        )
                    )
                }).padding()
            } else {
                Text(model.result?.title ?? "")
                    .bold()
                    .padding()
                Text(model.result?.component3() ?? "")
                    .padding()
            }
            
            if model.isLoading {
                Text("Loading...")
            }
            
            if !model.error.isEmpty {
                Text(model.error)
                    .foregroundColor(Color.red)
            }
            
            Spacer()
            
            if model.result == nil {
                Button(
                    action: { self.model.acceptAction(action: CreateAction.Create()) },
                    label: { Text("Create") }
                    ).padding()
                    .disabled(model.isLoading)
            }
    }.navigationBarTitle(Screen.createTask.description()).navigationBarBackButtonHidden(true)
        .navigationBarItems(leading: Button(
            action: { self.mainStore().acceptAction(
                action: MainAction.NavigateBack()
            ) },
            label: { Text("< Back") }
        ))
        .onAppear() {
            self.model.onAppear()
        }.onDisappear() {
            self.model.onDisappear()
        }
    }
}

struct CreateTaskView_Previews: PreviewProvider {
    static var previews: some View {
        CreateTaskView(mainStore: { MainStore(
        reducer: MainReducer(),
        middleware: [],
        initialState: MainStateProvider(restoredState: nil),
        stateSubjectProvider: DefaultStateSubjectProvider(),
        restoredState: nil)})
    }
}
