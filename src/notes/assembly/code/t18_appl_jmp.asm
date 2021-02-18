; 分析一个奇怪的程序 
assume cs:code

; 运行前思考 程序可以正确返回吗?	
; 运行后思考 为什么是这种结果?
code segment
			
	b4:		mov ax,4c00h
			int 21h

	start:	mov ax,0
	s:		nop					; nop指令: 什么都不做 占用一个字节
	b2:		nop

	b3:		mov di,offset s
			mov si,offset s2
			mov ax,cs:[si]
	b1:		mov cs:[di],ax		; 这四行 是把s2处一条指令 复制到s处

	s0:		jmp short s

	s1:		mov ax,0
			int 21h
			mov ax,0

	s2:		jmp short s1		; 短转移 机器码为EBXX
			nop

code ends

end start

程序已经结束 往后的内容编译器直接忽略
分析 b*为分析过程中添加的标记
	1 编译完成后 s2位置jmp的机器码为EBF6(向前跳10, -10=F6H)
	2 执行完b1行后 s和b2位置为 jmp short s1=EBF6 
	3 执行完s0后 接着执行s处新指令(jmp short s1=EBF6)
	4 而jmp后一行(b3行)的 指令偏移地址为 000AH=10
	5 则进行加法 F6H+10=0 IP寄存器变为0
	6 执行b4处指令 程序结束


	