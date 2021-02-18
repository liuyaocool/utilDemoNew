; dup 伪指令 重复
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

; -------- 给出数据的方式 --------------------------
			mov ax,1		; 立即数
			mov al,'a'
			mov ax,0FFH
			add al,00001111B

; -------- 数据的传递 --------------------------
			mov ax,dx
			push bx			; 栈
			pop ax
			push ds:[0]
			pop es:[0]


; -------- 数据从哪里来 --------------------------
			mov ax,ds:[0]
			mov ax,es:[0]
			mov ax,ss:[0]
			mov ax,cs:[0]

			mov ax,[bx]		; 默认段地址ds
			mov ax,ds:[si]
			mov ax,ds:[si]
			mov ax,[bp]		; 默认段地址为ss

			mov ax,ds[bx+si]
			mov ax,ds[bx+di]

			mov ax,ds:[bp+si]
			mov ax,ds:[bp+di]

			mov ax,ds:[bx+di+6]
			mov ax,ds:[bx+si+5]
			mov ax,ds:[bx+si+5+12*6]

			mov ax,ds:[bp+si+5]
			mov ax,ds:[bp+di+5]


			mov ax,4c00H
			int 21H
code ends

end start