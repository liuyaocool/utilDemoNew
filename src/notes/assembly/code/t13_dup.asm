; dup 伪指令 重复
assume cs:code,ds:data,ss:stack

data segment
	db		128	dup (0)				; 重复100个字节 都为0
	db		100	dup ('1')
	db		100	dup ('12')
	db		10 	dup ('123','asd')
	dw 		100	dup (1)
	dd 		10	dup (1)
data ends

stack segment stack
	db		128	dup (0)
stack ends


code segment

	start:	mov bx,stack
			mov ss,bx
			mov sp,128
			
			mov bx,data
			mov ds,bx




			mov ax,4c00H
			int 21H
code ends

end start