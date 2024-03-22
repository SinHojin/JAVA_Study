package main;

import daelim.spring_ch03.*;
import daelim.spring_ch03.config.AppContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import daelim.spring_ch03.config.Appconf1;
import daelim.spring_ch03.config.Appconf2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainForSpring {

    static ApplicationContext ctx;
    public static void main(String[] args) throws IOException {

        // AnnotationConfigApplicationContext를 이용해 스프링 컨테이너 생성
        //ctx = new AnnotationConfigApplicationContext(AppContext.class);
        ctx = new AnnotationConfigApplicationContext(Appconf1.class, Appconf2.class);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.println("명령어를 입력하세요.");
            String command = reader.readLine();
            if(command.equalsIgnoreCase("exit")) {
                System.out.println("종료합니다.");
                break;
            }
            if(command.startsWith("new")) {
                processNewCommand(command.split(" "));
                continue;
            } else if(command.startsWith("change")) {
                processChangeCommand(command.split(" "));
                continue;
            } else if(command.startsWith("list")) {
                processListCommand();
                continue;
            } else if(command.startsWith("info")) {
                processInfoCommand(command.split(" "));
                continue;
            } else if(command.startsWith("version")) {
                processVersionCommand();
                continue;
            }
            printHelp();
        }
    }

    private static void processNewCommand(String[] arg) {
        if(arg.length != 5) {
            printHelp();
            return;
        }

        // getBean() 메서드를 이용해 사용할 객체를 가져온다.
        MemberRegisterService memberRegisterService = ctx.getBean("memberRegisterService", MemberRegisterService.class);
        RegisterRequest req = new RegisterRequest();
        req.setEmail(arg[1]);
        req.setName(arg[2]);
        req.setPassword(arg[3]);
        req.setConfirmPassword(arg[4]);

        if(!req.isPasswordEqualToConfirmPassword()) {
            System.out.println("암호와 확인이 일치하지 않습니다.");
            return;
        }

        try {
            memberRegisterService.regist(req);
            System.out.println("등록완료!");
        } catch (DuplicationMemberException e) {
            System.err.println("이미 존재하는 이메일입니다.");
        }
    }

    private static void processChangeCommand(String[] arg) {
        if(arg.length != 4) {
            printHelp();
            return;
        }
        ChangePasswordService changePasswordService = ctx.getBean("changePasswordService", ChangePasswordService.class);
        try {
            changePasswordService.changePassword(arg[1], arg[2], arg[3]);
            System.out.println("암호를 변경했습니다.");
        } catch (MemberNotFoundException e) {
            System.err.println("존재하지 않는 이메일입니다.");
        } catch (WrongPasswordException e) {
            System.err.println("이메일과 암호가 일치하지 않습니다.");
        }
    }

    private static void printHelp() {
        System.out.println();
        System.out.println("잘못된 명령입니다. 아래 명령어 사용법을 확인하세요.");
        System.out.println("new 이메일 이름 암호 암호확인");
        System.out.println("change 이메일 현재암호 변경암호");
        System.out.println("list 전체 회원 리스트");
        System.out.println("info 회원 정보");
        System.out.println("version 버전 정보");
        System.out.println();
    }

    private static void processListCommand() {
        MemberListPrinter memberListPrinter = ctx.getBean("memberListPrinter", MemberListPrinter.class);
        memberListPrinter.printAll();
    }

    private static void processInfoCommand(String[] arg){
        if(arg.length !=2){
            printHelp();
            return;
        }

        MemberInfoPrinter memberInfoPrinter = ctx.getBean("memberInfoPrinter", MemberInfoPrinter.class);
        memberInfoPrinter.printMember(arg[1]);
    }

    private static void processVersionCommand(){
        VersionPrinter versionPrinter = ctx.getBean("versionPrinter", VersionPrinter.class);
        versionPrinter.print();
    }
}