```
为了描述上的简洁，在以后的课程中，
我们将使用两个描述性的符号reg来表示
一个寄存器，用sreg表示一个段寄存器。

reg的集合包括: ax、bx、cx、dx、ah、
al、bh、bl、ch、cl、dh、dl、sp、bp、
si、di ;
sreg的集合包括:ds、ss、cs、es。
```



```
8.1bx、si、di、bp
正确的用法:
mov ax,[bx]
mov ax,[bp+si] 
mov ax,[si]
mov ax,[bp+di]
mov ax,[di]
mov ax,[bx+si+idata]
mov ax,[bp]
mov ax,[bx+di+idata]
mov ax,[bx+si]
mov ax,[bp+si+idata]
mov ax,[bx+di]
mov ax,[bp+di+idata]
```

