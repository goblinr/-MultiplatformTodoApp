//
//  TaskView.swift
//  TodoApp
//
//  Created by Roman Romanov on 09.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

struct TaskPresentable : Identifiable {
    var id: String
    var title: String
    var description: String
    var status: TaskStatus
}

struct TaskView: View {
    var task: TaskPresentable
    
    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(task.title)
                    .font(.title)
                    .strikethrough(task.status == TaskStatus.done)
                Text(task.description)
                    .font(.subheadline)
                    .strikethrough(task.status == TaskStatus.done)
            }
            Spacer()
        }
        .padding()
    }
}

struct TaskView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            TaskView(
                task: TaskPresentable(
                    id: "id",
                    title: "title",
                    description: "description",
                    status: TaskStatus.pending
                )
                ).previewLayout(PreviewLayout.fixed(width: CGFloat(300), height: CGFloat(80)))
                .previewDisplayName("Pending")
            
            TaskView(
                task: TaskPresentable(
                    id: "id",
                    title: "title",
                    description: "description",
                    status: TaskStatus.done
                )
            ).previewLayout(PreviewLayout.fixed(width: CGFloat(300), height: CGFloat(80)))
            .previewDisplayName("Done")
        }
    }
}
