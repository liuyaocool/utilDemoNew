; push指令 将a段前8个字形数据,逆序到b段
assume cs:code

a segment
	dw		1,2,3,4,5,6,7,8,0AH,0BH,0CH,0DH,0EH,0FH,0FFH
a ends

b segment
	dw		0,0,0,0,0,0,0,0
b ends

code segment

start:		mov ax,b
			mov ss,ax
			mov sp,16
			
			mov ax,a
			mov ds,ax

			mov bx,0
			mov cx,8

pushAB:		push ds:[bx]
			add bx,2
			loop pushAB

			mov ax,4c00H
			int 21h
code ends

end start				; 定义代码结束