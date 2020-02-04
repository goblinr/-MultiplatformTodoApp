//
//  Container.swift
//  TodoApp
//
//  Created by Roman Romanov on 04.02.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import Foundation
import Interaction

class Container<S: State, A: Action> {
    
    private let schedulers: Schedulers
    private let closure: (S) -> Void
    
    private var disposable: Disposable? = nil
    private var store: Store<S, A>? = nil
    
    init(_ schedulers: Schedulers, closure: @escaping (S) -> Void) {
        self.schedulers = schedulers
        self.closure = closure
    }
    
    func onAppear(store: Store<S, A>) {
        dispose()
        self.store = store
        let states = IosObserwableWrapper<S>(inner: store.states())
        disposable = states
            .observeOn(scheduler: schedulers.main)
            .subscribe(isThreadLocal: true, onSubscribe: nil, onError: nil, onComplete: nil) { state in
            self.closure(state!)
        }
    }
    
    func onDisappear() {
        dispose()
        self.store = nil
    }
    
    func acceptAction(action: A) {
        store?.acceptAction(action: action)
    }
    
    private func dispose() {
        if disposable != nil && disposable?.isDisposed == false {
            disposable?.dispose()
        }
    }
}
