//
//  CreateTaskModel.swift
//  TodoApp
//
//  Created by Roman Romanov on 14.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

struct CreateTaskViewModel : ViewState {
    let isLoading: Bool
    let error: String
    let title: String
    let description: String
    let result: Task?
    let onTitleChanged: (String) -> Void
    let onDescriptionChanged: (String) -> Void
    let onSubmitClicked: () -> Void
    let onBack: () -> Void
    
    init(
        isLoading: Bool = false,
        error: String = "",
        title: String = "",
        description: String = "",
        result: Task? = nil,
        onTitleChanged: @escaping (String) -> Void = {_ in },
        onDescriptionChanged: @escaping (String) -> Void = {_ in },
        onSubmitClicked: @escaping () -> Void = {},
        onBack: @escaping () -> Void = {}
    ) {
        self.isLoading = isLoading
        self.error = error
        self.title = title
        self.description = description
        self.result = result
        self.onTitleChanged = onTitleChanged
        self.onDescriptionChanged = onDescriptionChanged
        self.onSubmitClicked = onSubmitClicked
        self.onBack = onBack
    }
}

extension CreateState {
    func toViewModel(
        actions: @escaping (Action) -> KotlinUnit,
        mainActions: @escaping (MainAction) -> Void
    ) -> CreateTaskViewModel {
        return CreateTaskViewModel(
            isLoading: isLoading,
            error: error,
            title: title,
            description: component4(),
            result: result,
            onTitleChanged: { value in
                _ = actions(
                    CreateAction.UpdateField(
                        value: value,
                        type: FieldType.title
                    )
                )
            },
            onDescriptionChanged: { value in
                _ = actions(
                    CreateAction.UpdateField(
                        value: value,
                        type: FieldType.description_
                    )
                )
            },
            onSubmitClicked: {
                _ = actions(CreateAction.Create())
            },
            onBack: {
                mainActions(MainAction.NavigateBack())
            }
        )
    }
}
