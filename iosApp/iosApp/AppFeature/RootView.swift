//
//  RootView.swift
//

import SwiftUI

struct RootView: View {
    var body: some View {
        NavigationView {
           List {
               NavigationLink(destination: TutorialView()) {
                   Text("Tutorial")
               }
               NavigationLink(destination: GithubSearchView()) {
                   Text("Github Search")
               }
           }
           .navigationBarTitle("KMM Sample App")
           .navigationBarTitleDisplayMode(.inline)
       }
    }
}

struct RootView_Previews: PreviewProvider {
    static var previews: some View {
        RootView()
    }
}
