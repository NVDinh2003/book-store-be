//package com.nvd.entity;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.validator.constraints.Length;
//
//import java.util.Collection;
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Entity
//// ràng buộc duy nhất trên cột "username" trong bảng "customer", không được phép có nhiều bản ghi trong bảng có cùng giá trị "username".
//@Table(name = "customer", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
//public class Customer {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "customer_id", nullable = false)
//    private Long id;
//
//    @NotEmpty(message = "First name can't be empty!")
//    private String firstName;
//    @NotEmpty(message = "Last name can't be empty!")
//    private String lastName;
//
//    public String getFirstName() {
//        return firstName + "." + lastName;
//    }
//
//    @Email(message = "*Please provide a valid Email")   // Check username có đúng dạng email hay không
//    @NotEmpty(message = "*Please provide an email")
//    private String username;
//    @Length(min = 5, message = "*Your password must have at least 5 characters")
//    @NotEmpty(message = "*Please provide your password")
//    private String password;
//    private String phone;
//
//    private boolean isDeleted;
//
//    //--------Address information----------//
//    private String company;
//    @NotNull(message = "Enter address 1!")
//    private String address1;
//    @NotNull(message = "Enter address 2!")
//    private String address2;
//    @NotNull(message = "Enter city!")
//    private String city;
//    @NotNull(message = "Enter state!")
//    private String state;
//    @NotNull(message = "Enter postal code!")
//    private String postalCode;
//
//    // Country
//    @NotNull(message = "Select Country!")
//    // (Country) sẽ được truy xuất tức thì (eager loading) khi Customer được truy xuất.
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
//    private Country country;
//    //--------End Address information----------//
//
//
//    //------------ Mapped table, column -----------//
//    // Role
//    // EAGER: truy xuất tức thì (eager loading) khi Customer được truy xuất.
//    // thực hiện thêm, sửa, xóa, cập nhật Customer, các thay đổi tương tự sẽ áp dụng cho các đối tượng Role mà customer liên quan đến.
//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "customer_roles",    // tên bảng liên kết customer-role
//            // joinColumns: xác định cột liên ết với table hiện tại đang đứng
//            joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"),
//            // inverseJoinColumns: xác định cột liên kết tới bảng..
//            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
//    private Collection<Role> roles;
//
//    // Shopping cart
//    @OneToOne(mappedBy = "customer")
//    private ShoppingCart shoppingCart;
//
//    // Order
//    @OneToMany(mappedBy = "customer")
//    private List<Order> orders;
//    //--------------------------------------//
//
//}