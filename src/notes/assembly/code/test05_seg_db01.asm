; a段与b段依次相加 存到c段
assume cs:code

a segment
	db		1,2,3,4,5,6,7,8	; db=define byte 字节型数据
a ends

b segment
	db		1,2,3,4,5,6,7,8
b ends

c segment
	db		0,0,0,0,0,0,0,0
c ends

code segment

start:		mov ax,c
			mov es,ax

			mov bx,0
			mov cx,8

sumAB:		mov ax,a
			mov ds,ax
			
			mov dx,0
			mov dl,ds:[bx]

			mov ax,b
			mov ds,ax
			
			add dl,ds:[bx]
			mov es:[bx],dl
			inc bx
			loop sumAB

			mov ax,4c00H
			int 21h
code ends

end start				; 定义代码结束