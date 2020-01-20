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
    
    @ObservedObject var model: CreateTaskModel
    var mainStore: Store<MainState, MainAction>
    
    @SwiftUI.State private var title = ""
    @SwiftUI.State private var description = ""
    
    private let createTaskAssembly = CreateTaskAssembly.instance()
    
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
            action: { self.mainStore.acceptAction(
                action: MainAction.NavigateBack()
            ) },
            label: { Text("< Back") }
        )).onAppear() {
            self.model.onAppear(store: self.createTaskAssembly.createTaskStore)
        }.onDisappear() {
            self.model.onDisappear()
        }
    }
}

#if DEBUG
struct CreateTaskView_Previews: PreviewProvider {
    
    static var defaultState: CreateTaskModel {
        get {
            let model = CreateTaskModel()
            return model
        }
    }
    
    static var loadingState: CreateTaskModel {
        get {
            let model = CreateTaskModel()
            model.isLoading = true
            return model
        }
    }
    
    static var errorState: CreateTaskModel {
        get {
            let model = CreateTaskModel()
            model.error = "Error"
            return model
        }
    }
    
    static var resultState: CreateTaskModel {
        get {
            let model = CreateTaskModel()
            model.result = Task(
                id: "id",
                title: "title",
                description: "description",
                status: TaskStatus.pending
            )
            return model
        }
    }
    
    static var previews: some View {
        Group {
            CreateTaskView(
                model: defaultState,
                mainStore: MainAssemly.instance().mainStore
            ).previewDisplayName("Default")
            
            CreateTaskView(
                model: loadingState,
                mainStore: MainAssemly.instance().mainStore
            ).previewDisplayName("Loading")
            
            CreateTaskView(
                model: errorState,
                mainStore: MainAssemly.instance().mainStore
            ).previewDisplayName("Error")
            
            CreateTaskView(
                model: resultState,
                mainStore: MainAssemly.instance().mainStore
            ).previewDisplayName("Result")
        }
    }
}
#endif
