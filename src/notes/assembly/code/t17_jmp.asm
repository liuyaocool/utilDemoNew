; jmp指令
assume cs:code,ds:data,ss:stack

; 
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

s:			mov ax,1000H
			db		130	dup (0)
			jmp near ptr s 		; 16位位移 -32768~32767

s1:			mov ax,100H
			db		120	dup (0)
			jmp short s 		; 8位位移 -128~127
			

			mov ax,4c00H
			int 21H
code ends

end start