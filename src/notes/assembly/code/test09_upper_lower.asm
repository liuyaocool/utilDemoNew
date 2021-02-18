
; 大小写转换
assume cs:code,ds:data,ss:stack

data segment
					; B 42H 0100 0010
					; b 62H	0110 0010

					; x     1101 1111
					; B and x = B; b and x = B
			db 		'BaSic' ; 转换为大写

					; y     0010 0000
					; B or y = b; b or y = b
			db 		'adfGagaASDeAdf' ; 转换为小写 14个
data ends

code segment

	start:	mov bx,data
			mov ds:bx

			mov bx:0
			mov cx:5
upperStr:	mov al,ds:[bx]
			and al,11011111B
			mov ds:[bx],al
			inc bx
			loop upperStr

			mov cx:14
lowerStr:	mov al,ds:[bx]	; bx不需要再归0 因为upperStr已经到了5
			or al,00100000B
			mov ds:[bx],al
			inc bx
			loop lowerStr

			mov ax,4c00H
			int 21h
code ends

end start