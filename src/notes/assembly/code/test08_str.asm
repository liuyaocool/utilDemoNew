; 前言
; - 信源 发出消息的源头
; - 编码 消息的约定
;   - ASCLL码
; - 信道(噪声) 消息传播的途径
; - 译码 消息按照约定翻译
; - 信宿 消息到达目的地

; 以字符形式给出数据
; ASCLL码

assume cs:code,ds:data,ss:stack

data segment
			db 		'12345a sdf !' ; 单引号
data ends

stack segment stack
			dw		0,0,0,0,0,0,0,0
			dw		0,0,0,0,0,0,0,0
stack ends

code segment

	start:	mov al,'a' ; 单引号
			and ah,'b'

			mov ax,4c00H
			int 21h
code ends

end start