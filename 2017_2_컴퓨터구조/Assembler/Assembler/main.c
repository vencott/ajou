//
//  main.c
//  Assembler
//
//  Created by 김정민 on 2017. 11. 17..
//  Copyright © 2017년 김정민. All rights reserved.
//

/*
 참고
 0 ADDIU  I done
 1 ADDU   R done
 2 AND    R done
 3 ANDI   I done
 4 BEQ    I done
 5 BNE    I done
 6 J      J done
 7 JAL    J done
 8 JR     R done
 9 LUI    I done
 10 LW    I done
 11 LA    ? done
 12 NOR   R done
 13 OR    R done
 14 ORI   I done
 15 SLTIU I done
 16 SLTU  R done
 17 SLL   R done
 18 SRL   R done
 19 SW    I done
 20 SUBU  R done
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void instructionAnalysis(char *instruction);

void Rformat(char *instruction, int type);
void Iformat(char *instruction, int type);
void Jformat(char *instruction, int type);
void LA(char *instruction);

void removeTabs(char *str);
void removeSpaces(char *str);
void removeNewlines(char *str);
void removeDollars(char *str);

void decToBinary(unsigned int num, char immediate[]);
void decToBinary_full(unsigned int num, char immediate[]);
void decToBinary_jump(unsigned int num, char immediate[]);
int HextoDEC(int hex);
char* negativeToBinary(int x);

void addCode(char* code);

static const char *REGISTER[32] = {"00000", "00001", "00010", "00011", "00100", "00101", "00110", "00111", "01000", "01001", "01010", "01011", "01100", "01101", "01110", "01111",
    "10000", "10001", "10010", "10011", "10100", "10101", "10110", "10111", "11000", "11001", "11010", "11011", "11100", "11101", "11110", "11111"};

static char *DATA_LABELS[1000];
static char *TEXT_LABELS[1000];

static const char *OPCODE_R = "000000";
static const char *SHAMPT_0 = "00000";

static const char *FUNCT_ADDU = "100001";
static const char *FUNCT_AND  = "100100";
static const char *FUNCT_OR   = "100101";
static const char *FUNCT_NOR  = "100111";
static const char *FUNCT_SLL  = "000000";
static const char *FUNCT_SRL  = "000010";
static const char *FUNCT_SUBU = "100011";
static const char *FUNCT_JR   = "001000";
static const char *FUNCT_SLTU = "101011";

static int labels_address[50];
static int location = 0;

static unsigned int DATA_NUM;
static unsigned int TEXT_NUM;

static char binaryCode[100000];

static char text_size[1000];
static char data_size[1000];

static int insturction_address = -1;
static int la_num = 0;

int main(int argc, const char * argv[]) {
    FILE* file_in;
    FILE* file_out;
    
    char line[50];
    char assemblyCode[50][50];
    
    int endIndex = 0;
    int dataStartIndex = 0;
    int textStartIndex = 0;
    int temp_index = 0;
    
    // ********************************************* Please put the absolute directory of assembly code file(.s) *********************************************
    if ((file_in = fopen("/Users/Kimjungmin/SCE212_201321062/Assembler/Assembler/example1.s", "r")) != NULL) {
        while(fgets(line, 50, file_in) != NULL) {
            strcpy(assemblyCode[endIndex], line);
            endIndex++;
        }
    }
    
    // check data & text section index
    for(int j = 0; j < endIndex; j++) {
        if(!strcmp(assemblyCode[j], "\t.data\n"))
            dataStartIndex = j;
        if(!strcmp(assemblyCode[j], "\t.text\n"))
            textStartIndex = j;
    }
    DATA_NUM = textStartIndex - dataStartIndex - 1;
    TEXT_NUM = 0;
    
    // copy data section
    for(int i = dataStartIndex + 1; i < textStartIndex; i++) {
        DATA_LABELS[i-1] = assemblyCode[i];
    }
    
    for(int i = textStartIndex + 1; i < endIndex; i++) {
        if(!strchr(assemblyCode[i], ':')) { // instruction
            insturction_address++;
            if(strstr(assemblyCode[i], "la\t$")) {
                la_num++;
                if(la_num > 1)
                    insturction_address++;
            }
        }
        if(strchr(assemblyCode[i], ':'))  {// label
            labels_address[temp_index] = insturction_address+1;
            TEXT_LABELS[temp_index] = assemblyCode[i];
            temp_index++;
        }
    }

    // text section
    for(int i = textStartIndex + 1; i < endIndex; i++) {
        if(!strchr(assemblyCode[i], ':')) // instruction
            instructionAnalysis(assemblyCode[i]);
        
        location++;
    }
    
    // ************************************************ Please put the name of assembly code file(.o) ************************************************
    if((file_out = fopen("test.o", "a")) == NULL)
        printf("FILE OUT ERROR\n");
    
    decToBinary_full(TEXT_NUM * 4, text_size);
    fprintf(file_out, "%s\n", text_size);
    decToBinary_full(DATA_NUM * 4, data_size);
    fprintf(file_out, "%s\n", data_size);

    // initial value of the data section
    for(int i = dataStartIndex + 1; i < textStartIndex; i++) {
        char* temp = assemblyCode[i];
        char* value;
        char out[] = "33333333333333333333333333333333";
        unsigned int num;
        
        removeTabs(temp);
        removeSpaces(temp);
        removeNewlines(temp);
        
        temp = temp + 3;
        strtok(temp, "d");
    
        value = strtok(NULL, "d");
        
        if(strstr(value, "0x"))
            num = (unsigned int)strtol(value, NULL, 0);
        else
            num = (unsigned int)atoi(value);
        
        decToBinary_full(num, out);
        
        addCode(out); addCode("\n"); TEXT_NUM++;
    }
    
    fprintf(file_out, "%s", binaryCode);
    
    fclose(file_in);
    fclose(file_out);
    
    printf("Assemble done.\n");
    
    return 0;
}

void instructionAnalysis(char *instruction) {
    removeTabs(instruction);
    removeSpaces(instruction);
    removeNewlines(instruction);
    
    if(strstr(instruction, "addiu"))
        Iformat(instruction, 0);
    else if(strstr(instruction, "addu"))
        Rformat(instruction, 1);
    else if(strstr(instruction, "andi"))
        Iformat(instruction, 3);
    else if(strstr(instruction, "and"))
        Rformat(instruction, 2);
    else if(strstr(instruction, "beq"))
        Iformat(instruction, 4);
    else if(strstr(instruction, "bne"))
        Iformat(instruction, 5);
    else if(strstr(instruction, "jal"))
        Jformat(instruction, 7);
    else if(strstr(instruction, "jr"))
        Rformat(instruction, 8);
    else if(strstr(instruction, "j"))
        Jformat(instruction, 6);
    else if(strstr(instruction, "lui"))
        Iformat(instruction, 9);
    else if(strstr(instruction, "lw"))
        Iformat(instruction, 10);
    else if(strstr(instruction, "la"))
        LA(instruction);
    else if(strstr(instruction, "nor"))
        Rformat(instruction, 12);
    else if(strstr(instruction, "ori"))
        Iformat(instruction, 14);
    else if(strstr(instruction, "or"))
        Rformat(instruction, 13);
    else if(strstr(instruction, "sltiu"))
        Iformat(instruction, 15);
    else if(strstr(instruction, "sltu"))
        Rformat(instruction, 16);
    else if(strstr(instruction, "sll"))
        Rformat(instruction, 17);
    else if(strstr(instruction, "srl"))
        Rformat(instruction, 18);
    else if(strstr(instruction, "sw"))
        Iformat(instruction, 19);
    else if(strstr(instruction, "subu"))
        Rformat(instruction, 20);
    else
        printf("=========ERROR=======");
    
    //printf("%d\n", location);
}

void Rformat(char *instruction, int type) {
    char* opcode = OPCODE_R;
    char* rs = "00000";
    char* rt;
    char* rd;
    char* shamt = SHAMPT_0;
    char* funct;
    
    switch (type) {
        case 1: // ADDU
            funct = FUNCT_ADDU;
            instruction = instruction + 4;
            removeDollars(instruction);
            rd = REGISTER[atoi(strtok(instruction, ","))];
            rs = REGISTER[atoi(strtok(NULL, ","))];
            rt = REGISTER[atoi(strtok(NULL, ","))];
            break;
        case 2: // AND
            funct = FUNCT_AND;
            instruction = instruction+3;
            removeDollars(instruction);
            rd = REGISTER[atoi(strtok(instruction, ","))];
            rs = REGISTER[atoi(strtok(NULL, ","))];
            rt = REGISTER[atoi(strtok(NULL, ","))];
            break;
        case 12: // NOR
            funct = FUNCT_NOR;
            instruction = instruction+3;
            removeDollars(instruction);
            rd = REGISTER[atoi(strtok(instruction, ","))];
            rs = REGISTER[atoi(strtok(NULL, ","))];
            rt = REGISTER[atoi(strtok(NULL, ","))];
            break;
        case 13: //OR
            funct = FUNCT_OR;
            instruction = instruction+2;
            removeDollars(instruction);
            rd = REGISTER[atoi(strtok(instruction, ","))];
            rs = REGISTER[atoi(strtok(NULL, ","))];
            rt = REGISTER[atoi(strtok(NULL, ","))];
            break;
        case 17: //SLL
            funct = FUNCT_SLL;
            instruction = instruction+3;
            removeDollars(instruction);
            rd = REGISTER[atoi(strtok(instruction, ","))];
            rt = REGISTER[atoi(strtok(NULL, ","))];
            shamt = REGISTER[atoi(strtok(NULL, ","))];
            break;
        case 18: //SRL
            funct = FUNCT_SRL;
            instruction = instruction+3;
            removeDollars(instruction);
            rd = REGISTER[atoi(strtok(instruction, ","))];
            rt = REGISTER[atoi(strtok(NULL, ","))];
            shamt = REGISTER[atoi(strtok(NULL, ","))];
            break;
        case 20: //SUBU
            funct = FUNCT_SUBU;
            instruction = instruction+4;
            removeDollars(instruction);
            rd = REGISTER[atoi(strtok(instruction, ","))];
            rs = REGISTER[atoi(strtok(NULL, ","))];
            rt = REGISTER[atoi(strtok(NULL, ","))];
            break;
        case 8: //JR
            funct = FUNCT_JR;
            instruction = instruction+2;
            removeDollars(instruction);
            rs = REGISTER[atoi(instruction)];
            rd = "00000";
            rt = "00000";
            break;
        case 16: //SLTU
            funct = FUNCT_SLTU;
            instruction = instruction+4;
            removeDollars(instruction);
            rd = REGISTER[atoi(strtok(instruction, ","))];
            rs = REGISTER[atoi(strtok(NULL, ","))];
            rt = REGISTER[atoi(strtok(NULL, ","))];
            break;
        default:
            break;
    }
    
    addCode(opcode); addCode(rs); addCode(rt); addCode(rd); addCode(shamt); addCode(funct); addCode("\n"); TEXT_NUM++;
}

void Iformat(char *instruction, int type) {
    char* opcode;
    char* rs;
    char* rt;
    char* immediate;
    char immediate_b[16];
    int labelAddress;
    int offset;
    
    unsigned int num;
    
    switch (type) {
        case 0: // ADDIU
            opcode = "001001";
            instruction = instruction+5;
            removeDollars(instruction);
            rt = REGISTER[atoi(strtok(instruction, ","))];
            rs = REGISTER[atoi(strtok(NULL, ","))];
            immediate = strtok(NULL, ",");
            if(strstr(immediate, "0x"))
                num = (unsigned int)strtol(immediate, NULL, 0);
            else
                num = (unsigned int)atoi(immediate);
            
            decToBinary(num, immediate_b);
            break;
        case 4: // BEQ
            opcode = "000100";
            instruction = instruction+3;
            removeDollars(instruction);
            rs = REGISTER[atoi(strtok(instruction, ","))];
            rt = REGISTER[atoi(strtok(NULL, ","))];
            immediate = strtok(NULL, ",");
            
            for(int i = 0; i < 100; i++) {
                if(strstr(TEXT_LABELS[i], immediate) != NULL) {
                    labelAddress = labels_address[i];
                    break;
                }
            }
            
            offset = labelAddress - location;
            
            if(offset > 0)
                decToBinary(offset-1, immediate_b);
            else
                strncpy(immediate_b, negativeToBinary(offset), 16);
            break;
        case 5: // BNE
            opcode = "000101";
            instruction = instruction+3;
            removeDollars(instruction);
            rs = REGISTER[atoi(strtok(instruction, ","))];
            rt = REGISTER[atoi(strtok(NULL, ","))];
            immediate = strtok(NULL, ",");
            
            for(int i = 0; i < 100; i++) {
                if(strstr(TEXT_LABELS[i], immediate) != NULL) {
                    labelAddress = labels_address[i];
                    break;
                }
            }
            
            offset = labelAddress - location;
            
            if(offset > 0)
                decToBinary(offset-1, immediate_b);
            else
                strncpy(immediate_b, negativeToBinary(offset), 16);
            break;
        case 9: //LUI
            opcode = "001111";
            instruction = instruction+3;
            removeDollars(instruction);
            rt = REGISTER[atoi(strtok(instruction, ","))];
            rs = "00000";
            immediate = strtok(NULL, ",");
            if(strstr(immediate, "0x"))
                num = (unsigned int)strtol(immediate, NULL, 0);
            else
                num = (unsigned int)atoi(immediate);
            
            decToBinary(num, immediate_b);
            break;
        case 14: //ORI
            opcode = "001101";
            instruction = instruction+3;
            removeDollars(instruction);
            rt = REGISTER[atoi(strtok(instruction, ","))];
            rs = REGISTER[atoi(strtok(NULL, ","))];
            immediate = strtok(NULL, ",");
            if(strstr(immediate, "0x"))
                num = (unsigned int)strtol(immediate, NULL, 0);
            else
                num = (unsigned int)atoi(immediate);
            
            decToBinary(num, immediate_b);
            break;
        case 3: //ANDI
            opcode = "001100";
            instruction = instruction+4;
            removeDollars(instruction);
            rt = REGISTER[atoi(strtok(instruction, ","))];
            rs = REGISTER[atoi(strtok(NULL, ","))];
            immediate = strtok(NULL, ",");
            if(strstr(immediate, "0x"))
                num = (unsigned int)strtol(immediate, NULL, 0);
            else
                num = (unsigned int)atoi(immediate);
            
            decToBinary(num, immediate_b);
            break;
        case 10: //LW
            opcode = "100011";
            instruction = instruction+2;
            removeDollars(instruction);
            rt = REGISTER[atoi(strtok(instruction, ","))];
            char *temp = strtok(NULL, ",");
            immediate = strtok(temp, "(");
            rs = REGISTER[atoi(strtok(NULL, "("))];
            
            if(strstr(immediate, "-")) {
                num = (unsigned int)strtol(immediate, NULL, 0);
                strncpy(immediate_b, negativeToBinary(num), 16);
            }
            else {
                num = (unsigned int)atoi(immediate);
                decToBinary(num, immediate_b);
            }
            break;
        case 19: //SW
            opcode = "101011";
            instruction = instruction+2;
            removeDollars(instruction);
            rt = REGISTER[atoi(strtok(instruction, ","))];
            char *temp2 = strtok(NULL, ",");
            immediate = strtok(temp2, "(");
            rs = REGISTER[atoi(strtok(NULL, "("))];
            
            if(strstr(immediate, "-")) {
                num = (unsigned int)strtol(immediate, NULL, 0);
                strncpy(immediate_b, negativeToBinary(num), 16);
            }
            else {
                num = (unsigned int)atoi(immediate);
                decToBinary(num, immediate_b);
            }
            break;
        case 15: //SLTIU
            opcode = "001011";
            instruction = instruction+5;
            removeDollars(instruction);
            rt = REGISTER[atoi(strtok(instruction, ","))];
            rs = REGISTER[atoi(strtok(NULL, ","))];
            immediate = strtok(NULL, ",");
            if(strstr(immediate, "0x"))
                num = (unsigned int)strtol(immediate, NULL, 0);
            else
                num = (unsigned int)atoi(immediate);
            
            decToBinary(num, immediate_b);
            break;
        default:
            break;
    }

    addCode(opcode); addCode(rs); addCode(rt); addCode(immediate_b); addCode("\n"); TEXT_NUM++;
}

void Jformat(char *instruction, int type) {
    char* opcode;
    char* destination;
    char address[26];
    int dec;
    int hex;
    
    switch (type) {
        case 6: //J
            opcode = "000010";
            destination = strtok(instruction, "j");
            
            for(int i = 0; i < 100; i++) {
                if(strstr(TEXT_LABELS[i], destination) != NULL) {
                    hex = labels_address[i];
                    break;
                }
            }
            hex += 100000;
            dec = HextoDEC(hex);
            decToBinary_jump(dec, address);
            break;
        case 7: //JAL
            opcode = "000011";
            destination = instruction+3;
            
            for(int i = 0; i < 100; i++) {
                if(strstr(TEXT_LABELS[i], destination) != NULL) {
                    hex = labels_address[i];
                    break;
                }
            }
            hex += 100000;
            dec = HextoDEC(hex);
            decToBinary_jump(dec, address);
            break;
        default:
            break;
    }
    
    addCode(opcode); addCode(address); addCode("\n"); TEXT_NUM++;
}


void LA(char *instruction) {
    int index;
    
    char* opcode = "001111";
    char* rs = "00000";
    char* rt;
    char immediate[16] = "0001000000000000";
    
    char* label;
    strtok(instruction, ",");
    label = strtok(NULL, ",");
    for(int i = 0; i < DATA_NUM; i++) {
        if(strstr(DATA_LABELS[i], label))
            index = i;
    }
    instruction = instruction + 2;
    removeDollars(instruction);
    rt = REGISTER[atoi(instruction)];
    if(index == 0) {
        addCode(opcode); addCode(rs); addCode(rt); addCode(immediate); addCode("\n"); TEXT_NUM++;
    }
    else {
        addCode(opcode); addCode(rs); addCode(rt); addCode(immediate); addCode("\n"); TEXT_NUM++;
        rs = rt;
        opcode = "001101";
        index *=4;
        decToBinary(index, immediate);
        addCode(opcode); addCode(rs); addCode(rt); addCode(immediate); addCode("\n"); TEXT_NUM++;
    }
}

void decToBinary(unsigned int num, char immediate[]) {
    unsigned int mask=32768;
    int i = 0;
    while(mask > 0)
    {
        if((num & mask) == 0 )
            immediate[i] = '0';
        else
            immediate[i] = '1';
        mask = mask >> 1 ;
        i++;
    }
}

void decToBinary_full(unsigned int num, char immediate[]) {
    unsigned int mask = 2147483648;
    int i = 0;
    while(mask > 0)
    {
        if((num & mask) == 0 )
            immediate[i] = '0';
        else
            immediate[i] = '1';
        mask = mask >> 1 ;
        i++;
    }
}

void decToBinary_jump(unsigned int num, char immediate[]) {
    unsigned int mask = 33554432;
    int i = 0;
    while(mask > 0)
    {
        if((num & mask) == 0 )
            immediate[i] = '0';
        else
            immediate[i] = '1';
        mask = mask >> 1 ;
        i++;
    }
}

char* negativeToBinary(int x) {
    static char b[17];
    b[0] = '\0';
    
    int z;
    for (z = 32768; z > 0; z >>= 1)
    {
        strcat(b, ((x & z) == z) ? "1" : "0");
    }
    return b;
}

int HextoDEC(int hex) {
    int dec = 0;
    int remainder = 0;
    int count = 0;
    
    while(hex != 0)
    {
        remainder = hex % 10;
        dec = dec + remainder * pow(16, count);
        hex = hex / 10;
        count++;
    }
    return dec;
}

void removeTabs(char *str)
{
    int count = 0;
    
    for (int i = 0; str[i]; i++)
        if (str[i] != '\t')
            str[count++] = str[i];
    str[count] = '\0';
}

void removeSpaces(char *str)
{
    int count = 0;
    
    for (int i = 0; str[i]; i++)
        if (str[i] != ' ')
            str[count++] = str[i];
    str[count] = '\0';
}

void removeNewlines(char *str)
{
    int count = 0;
    
    for (int i = 0; str[i]; i++)
        if (str[i] != '\n')
            str[count++] = str[i];
    str[count] = '\0';
}

void removeDollars(char *str)
{
    int count = 0;
    
    for (int i = 0; str[i]; i++)
        if (str[i] != '$')
            str[count++] = str[i];
    str[count] = '\0';
}

void addCode(char* code) {
    strcat(binaryCode, code);
}
