; 条件转移指令
assume cs:code,ds:data,ss:stack

data segment
	db		128	dup (0)
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