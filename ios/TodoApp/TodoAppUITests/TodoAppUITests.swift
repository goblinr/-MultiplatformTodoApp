//
//  TodoAppUITests.swift
//  TodoAppUITests
//
//  Created by Roman Romanov on 09.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import XCTest

class TodoAppUITests: XCTestCase {
    
    let timeout = TimeInterval(10)

    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.

        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false

        // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testMainScreen() {
        // UI tests must launch the application that they test.
        let app = XCUIApplication()
        app.launchArguments = ["testing"]
        app.launch()

        XCTAssertTrue(app.wait(for: .runningForeground, timeout: timeout))
        XCTAssertTrue(app.staticTexts["One"].waitForExistence(timeout: timeout))
        XCTAssertTrue(app.staticTexts["Two"].waitForExistence(timeout: timeout))
    }
}
