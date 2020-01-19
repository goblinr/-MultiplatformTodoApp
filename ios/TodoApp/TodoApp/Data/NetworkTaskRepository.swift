//
//  NetworkTaskRepository.swift
//  TodoApp
//
//  Created by Roman Romanov on 13.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import Foundation
import Interaction
import Alamofire
import Alamofire_Synchronous
import SwiftyJSON

final class NetworkTaskRepository : AbstractTaskRepository {
    
    private let host = "http://192.168.1.123:8080/"
    
    override func blockingTasks(success: @escaping ([Task]) -> Void, error: @escaping (KotlinThrowable) -> Void) {
        let response = Alamofire.request("\(host)tasks").responseJSON()
        switch response.result {
        case .success(let value):
            print(value)
            let json = JSON(value)
            success(
                json.map { arg in
                    let (_, json) = arg
                    return FreezeKt.createTask(
                        id: json["id"].stringValue,
                        title: json["title"].stringValue,
                        description: json["description"].stringValue,
                        status: json["status"].stringValue
                    )
                }
            )

        case let .failure(message):
            error(KotlinThrowable(message: message.localizedDescription))
        }
    }
    
    override func blockingSwitchTask(id: String, success: @escaping () -> Void, error: @escaping (KotlinThrowable) -> Void) {
        let response = Alamofire.request("\(host)tasks/\(id)", method: .patch).responseJSON()
        
        switch response.result {
        case .success:
            success()
            
        case let .failure(message):
            error(KotlinThrowable(message: message.localizedDescription))
        }
    }
    
    override func blockingArchiveTask(success: @escaping () -> Void, error: @escaping (KotlinThrowable) -> Void) {
        let response = Alamofire.request("\(host)tasks/archive", method: .post).responseJSON()
        
        switch response.result {
        case .success:
            success()
            
        case let .failure(message):
            error(KotlinThrowable(message: message.localizedDescription))
        }
    }
    
    override func blockingUnarchiveTask(success: @escaping () -> Void, error: @escaping (KotlinThrowable) -> Void) {
        // TODO
    }
    
    override func blockingCreateTask(title: String, description: String, success: @escaping (Task) -> Void, error: @escaping (KotlinThrowable) -> Void) {
        let parameters: Parameters = [
            "title": title,
            "description": description
        ]
        let response = Alamofire.request(
            "\(host)tasks",
            method: .post,
            parameters: parameters,
            encoding: JSONEncoding.default
        ).responseJSON()
        
        switch response.result {
        case .success(let value):
            print(value)
            let json = JSON(value)
            success(
                FreezeKt.createTask(
                    id: json["id"].stringValue,
                    title: json["title"].stringValue,
                    description: json["description"].stringValue,
                    status: json["status"].stringValue
                )
            )

        case let .failure(message):
            error(KotlinThrowable(message: message.localizedDescription))
        }
    }
}

#if DEBUG
final class MockTaskRepository : AbstractTaskRepository {
    
    override func blockingTasks(success: @escaping ([Task]) -> Void, error: @escaping (KotlinThrowable) -> Void) {
        success(
            FreezeKt.freezeTaskList(list: [
                Task(id: "1", title: "One", description: "First task", status: TaskStatus.pending),
                Task(id: "2", title: "Two", description: "Second task", status: TaskStatus.done)
            ])
        )
    }
    
    override func blockingSwitchTask(id: String, success: @escaping () -> Void, error: @escaping (KotlinThrowable) -> Void) {
        
    }
    
    override func blockingArchiveTask(success: @escaping () -> Void, error: @escaping (KotlinThrowable) -> Void) {
        
    }
    
    override func blockingUnarchiveTask(success: @escaping () -> Void, error: @escaping (KotlinThrowable) -> Void) {
        
    }
    
    override func blockingCreateTask(title: String, description: String, success: @escaping (Task) -> Void, error: @escaping (KotlinThrowable) -> Void) {
        
    }
}
#endif
