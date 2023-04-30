//
//  GithubSearchView.swift
//  

import SwiftUI
import shared


class GithubSearchData: ObservableObject {
    @Published var searchText = ""
    @Published var result: SearchResult?
    
    func fetch() {
        GithubSearch().request(text: searchText) { result, error in
            DispatchQueue.main.async {
                self.result = result
            }
        }
    }
}

struct GithubSearchView: View {
    @StateObject var data: GithubSearchData = .init()
    
    var body: some View {
        VStack {
            // 検索項目
            TextField("user name", text: $data.searchText)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .keyboardType(.asciiCapable)
                .padding()
                .onSubmit(data.fetch)
            Spacer()
            // 検索結果リスト
            if let users = data.result?.users {
                ScrollView {
                    VStack {
                        ForEach(Array(users), id: \.self) { user in
                            UserCell(user: user)
                            Divider()
                        }
                    }
                }
            } else {
                Text("検索ワードに一致するユーザーが存在しません。")
            }
            Spacer()
        }
        .navigationTitle("Github User Search")
    }
}

struct UserCell: View {
    let user: User
    
    var body: some View {
        HStack {
            AsyncImage(url: URL(string: user.avatarUrl)) { image in
                image.resizable()
            } placeholder: {
                ProgressView()
            }
            .clipShape(Circle())
            .frame(width: 50, height: 50)
            .padding()
            
            Spacer()
            Text(user.login)
                .padding()
            Spacer()
        }
    }
}


struct GithubSearchView_Previews: PreviewProvider {
    static var previews: some View {
        GithubSearchView()
    }
}
