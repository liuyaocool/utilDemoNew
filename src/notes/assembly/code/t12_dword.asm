; 计算 data段 第一个数据 除以 第二个数据 商存放在第三个单元
assume cs:code,ds:data,ss:stack

; dd 4字节 32位 dword 双字型 double word
data segment
	dd 		100001	; 4字节
	dw 		100		; 2字节
	dw		0		; 1字节
	; db		1
data ends

stack segment stack
	dw 		0,0,0,0	
	dw 		0,0,0,0	
	dw 		0,0,0,0	
	dw 		0,0,0,0	
stack ends


code segment

	start:	mov bx,stack
			mov ss,bx
			mov sp,32
			
			mov bx,data
			mov ds,bx
			
			mov ax,ds:[0]	;低位
			mov dx,ds:[2]	;高位
			
			div word ptr ds:[4]

			mov ds:[6],ax

			; 问题
			mov ax,0FFFFH
			mov dx,1		; 1FFFFH /1 = 1FFFFH  → dx=1 ax=0FFFFH
			mov bx,1		; dx=余数 ax=商?
			div bx

			mov ax,4c00H
			int 21H
code ends

end start