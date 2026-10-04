#include <iostream>
using namespace std;

int main(){
    cout << "Your massive(size 1): "; int a; cin >> a; cout << endl;
    cout << "Ypur massive(size 2): "; int b; cin >> b;
    int arr[a][b];
    for(int i = 0; i < a; i++){
        for(int j = 0; j < b; j++){
            arr[i][j] = 1;
        }
    }
    for(int i = 0; i < a; i++){
        cout << endl;
        for(int j = 0; j < b; j++){
            cout << arr[i][j] << " ";
        }
    }
    return 0;
}