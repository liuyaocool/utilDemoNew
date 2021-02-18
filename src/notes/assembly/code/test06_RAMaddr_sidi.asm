; data 那句话 复制到下一行
assume cs:code,ds:data

data segment
	db		'welcome to masm!'
	db		'................'
data ends

code segment

start:		mov bx,data
			mov ds,bx

			mov si,0
			; mov di,16

			mov cx,8

moveAB:		mov ax,ds:[si]
			; mov ds:[di],ax
			mov ds:[si+16],ax
			add si,2
			add di,2
			loop moveAB

			mov ax,4c00H
			int 21h
code ends

end start