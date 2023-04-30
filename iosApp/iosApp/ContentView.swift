import SwiftUI
import shared

class SampleData: ObservableObject {
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

struct ContentView: View {
    @StateObject private(set) var data: SampleData = .init()

	var body: some View {
        Text(data.text)
            .onAppear {
                data.fetch()
            }
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
