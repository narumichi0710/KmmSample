//
//  TutorialView.swift
//

import SwiftUI
import shared

class TutorialData: ObservableObject {
    @Published var text = "Loading..."
    
    func fetch() {
        Greeting().greet { greeting, error in
           DispatchQueue.main.async {
               if let greeting = greeting {
                   self.text = greeting
               } else {
                   self.text = error?.localizedDescription ?? "error"
               }
           }
        }
    }
}

struct TutorialView: View {
    @StateObject var data: TutorialData = .init()

	var body: some View {
        Text(data.text)
            .onAppear {
                data.fetch()
            }
	}
}

struct TutorialView_Previews: PreviewProvider {
	static var previews: some View {
        TutorialView()
	}
}
