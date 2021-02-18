; 不使用寄存器确定数据长度
; mov word ptr ds:[0],1 ; 这里1为字型数据
; mov byte ptr ds:[0],1 ; 这里1为字节型数据
; add byte ptr ds:[0],1
; inc word ptr ds:[0]
assume cs:code,ds:data,ss:stack

data segment
	db 		'DEC'		; 公司名
	db 		'Ken 01sen'	; 总裁名
	dw 		137			; 排名		→ 38		12
	dw 		40			; 收入		→ +70		14
	db 		'PDP'		; 著名产品  	→ 'VAX'		16
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

			mov bx,0

			; 在这之前 字节型需要先放到字节寄存器 进行类似转格式的操作
			mov word ptr ds:[bx+12],38
			add word ptr ds:[bx+14],70

			mov si,0
			mov byte ptr ds:[bx+16+si],'V'
			inc si
			mov byte ptr ds:[bx+10H+si],'A'
			inc si
			mov byte ptr ds:[bx+16+si],'X'

			mov ax,4c00H
			int 21H
code ends

end start