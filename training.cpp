#include <iostream>
#include <string>
#include <vector>
using namespace std;

class Box{
    private:
        int hp = 0;
        string name;
    public:
        void usehp(int hhp){
            hp = hhp;
        }
        void print(){
            cout << hp;
        }
};
int main(){
    Box box;
    box.usehp(20);
    box.print();
}


