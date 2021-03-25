package com.isa.grpc.service;

import com.isa.grpc.protobuf.Employee;
import com.isa.grpc.protobuf.EmployeeServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@net.devh.boot.grpc.server.service.GrpcService
public class GrpcService extends EmployeeServiceGrpc.EmployeeServiceImplBase{

    private final Logger log = LoggerFactory.getLogger(GrpcService.class);

    @Override
    public void getEmployeeData(
            Employee.EmployeeRequest request,
            StreamObserver<Employee.EmployeeResponse> responseObserver) {

        String employeeEmail = request.getEmail().trim();

//        log.trace("Recived Email TRACE: {}",employeeEmail);
        log.debug("Recived Email DEBUG: {}",employeeEmail);
//        log.info("Recived Email INFO: {}",employeeEmail);
//        log.warn("Recived Email WARN: {}",employeeEmail);
//        log.error("Recived Email Error: {}",employeeEmail);

        if(!employeeEmail.isEmpty()){
            Employee.EmployeeResponse response;

            response =
                    Employee.EmployeeResponse.newBuilder()
                                .setFirstName("Nuwan")
                                .setLastName("Madhusanka")
                                .setEmployeeId(1)
                                .setDepartment("Dev")
                                .setJoinDate("2020/10/12")
                                .setMobile("0778978890")
                                .setTeam("Team")
                                .build();


                responseObserver.onNext(response);
        }else{
            responseObserver.onError(Status.INTERNAL.withDescription("Email cannot be empty").asException());
        }
        responseObserver.onCompleted();
    }
}
