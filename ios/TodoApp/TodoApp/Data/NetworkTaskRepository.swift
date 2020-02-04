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

final class NetworkTaskRepository : TaskRepository {
    
    private let host = "http://192.168.1.123:8080/"
    private let changedSubject = PublishSubjectBuilderKt.PublishSubject()
    
    func allTasks() -> Single {
        return SingleByEmitterKt.single { emitter in
            let response = Alamofire.request("\(self.host)tasks").responseJSON()
            switch response.result {
            case .success(let value):
                print(value)
                let result = JSON(value).map { (arg) -> Task in
                    let (_, json) = arg
                    return FreezeKt.createTask(
                        id: json["id"].stringValue,
                        title: json["title"].stringValue,
                        description: json["description"].stringValue,
                        status: json["status"].stringValue
                    )
                }
                emitter.onSuccess(value: result)

            case let .failure(message):
                emitter.onError(error: KotlinThrowable(message: message.localizedDescription))
            }
        }
    }
    
    func archiveTasks() -> Completable {
        return CompletableByEmitterKt.completable { emitter in
            let response = Alamofire.request("\(self.host)tasks/archive", method: .post).responseJSON()
            
            switch response.result {
            case .success:
                emitter.onComplete()
                self.changedSubject.onNext(value: nil)
                
            case let .failure(message):
                emitter.onError(error: KotlinThrowable(message: message.localizedDescription))
            }
        }
    }
    
    func changed() -> Observable {
        return changedSubject
    }
    
    func createTask(title: String, description: String) -> Single {
        return SingleByEmitterKt.single { emitter in
            let parameters: Parameters = [
                "title": title,
                "description": description
            ]
            let response = Alamofire.request(
                "\(self.host)tasks",
                method: .post,
                parameters: parameters,
                encoding: JSONEncoding.default
            ).responseJSON()
            
            switch response.result {
            case .success(let value):
                print(value)
                let json = JSON(value)
                emitter.onSuccess(value:
                    FreezeKt.createTask(
                        id: json["id"].stringValue,
                        title: json["title"].stringValue,
                        description: json["description"].stringValue,
                        status: json["status"].stringValue
                    )
                )
                self.changedSubject.onNext(value: nil)

            case let .failure(message):
                emitter.onError(error: KotlinThrowable(message: message.localizedDescription))
            }
        }
    }
    
    func switchTask(id: String) -> Completable {
        return CompletableByEmitterKt.completable { emitter in
            let response = Alamofire.request("\(self.host)tasks/\(id)", method: .patch).responseJSON()
            
            switch response.result {
            case .success:
                emitter.onComplete()
                self.changedSubject.onNext(value: nil)
                
            case let .failure(message):
                emitter.onError(error: KotlinThrowable(message: message.localizedDescription))
            }
        }
    }
    
    func unarchiveTask(id: String) -> Completable {
        return CompletableByEmitterKt.completable { emitter in
            // TODO
        }
    }
}

#if DEBUG
final class MockTaskRepository : TaskRepository {
    
    private let changedSubject = PublishSubjectBuilderKt.PublishSubject()
    
    func allTasks() -> Single {
        return SingleByEmitterKt.single { emitter in
            Thread.sleep(forTimeInterval: 2)
            emitter.onSuccess(value: FreezeKt.freezeTaskList(list: [
                Task(id: "1", title: "One", description: "First task", status: TaskStatus.pending),
                Task(id: "2", title: "Two", description: "Second task", status: TaskStatus.done)
            ]))
        }
    }
    
    func archiveTasks() -> Completable {
        return CompletableByEmitterKt.completable { emitter in
            emitter.onError(error: KotlinThrowable(message: "Error"))
        }
    }
    
    func changed() -> Observable {
        return changedSubject
    }
    
    func createTask(title: String, description: String) -> Single {
        return SingleByEmitterKt.single { emitter in
            emitter.onSuccess(value: FreezeKt.createTask(
                id: "id",
                title: title,
                description: description,
                status: TaskStatus.pending.description()
            ))
            self.changedSubject.onNext(value: nil)
        }
    }
    
    func switchTask(id: String) -> Completable {
        return CompletableByEmitterKt.completable { emitter in
            emitter.onError(error: KotlinThrowable(message: "Error"))
        }
    }
    
    func unarchiveTask(id: String) -> Completable {
        return CompletableByEmitterKt.completable { emitter in
            emitter.onComplete()
            self.changedSubject.onNext(value: nil)
        }
    }
}
#endif
