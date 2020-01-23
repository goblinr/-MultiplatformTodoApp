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
    static var data: Array<(key: String, value: TaskPresentable)> {
        return [
            (key: "Pending", value: TaskPresentable(
                id: "id",
                title: "title",
                description: "description",
                status: TaskStatus.pending
            )),
            (key: "Done", value: TaskPresentable(
                id: "id",
                title: "title",
                description: "description",
                status: TaskStatus.done
            ))
        ]
    }
    static var previews: some View {
        Group {
            ForEach(data, id: \.key) { entry in
                Group {
                    TaskView(
                        task: entry.value
                    ).previewLayout(PreviewLayout.fixed(width: CGFloat(300), height: CGFloat(80)))
                        .previewDisplayName(entry.key)
                    
                    TaskView(
                        task: entry.value
                    )
                    .darkModeFix()
                        .previewLayout(PreviewLayout.fixed(width: CGFloat(300), height: CGFloat(80)))
                        .previewDisplayName("\(entry.key) Dark")
                }
            }
        }
    }
}
