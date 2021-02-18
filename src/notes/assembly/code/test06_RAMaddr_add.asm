; a段与b段依次相加 存到c段
assume cs:code

a segment
	db		1,2,3,4,5,6,7,8
a ends

b segment
	db		1,2,3,4,5,6,7,8
b ends

c segment
	db		0,0,0,0,0,0,0,0
c ends

code segment

start:		mov bx,a
			mov ds,bx

			mov bx,0
			mov cx,8

sumAB:		mov al,ds:[bx]
			add al,ds:[bx+16] ;这里 每个段最小16字节
			mov ds:[bx+32],al
			inc bx
			loop sumAB

			mov ax,4c00H
			int 21h
code ends

end start