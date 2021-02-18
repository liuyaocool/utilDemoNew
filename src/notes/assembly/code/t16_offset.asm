; 操作符 offset
assume cs:code,ds:data,ss:stack

; 将s处一条指令复制到s0处
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

	s:		mov ax,bx 			; 占2个字节
			mov si,offset s 	; 取得s位置的偏移地址
			mov di,offset s0

			mov dx:cs:[si]
			mov cs:[si],bx

	s0:		nop
			nop

			mov ax,4c00H
			int 21H
code ends

end start