import { Moment } from 'moment';
import { IOrders } from 'app/shared/model//orders.model';

export interface ICustomerAddresses {
    id?: number;
    addressReference?: string;
    addressName?: string;
    addressNif?: string;
    streetAddress?: string;
    city?: string;
    state?: string;
    zipCode?: string;
    phoneNumber?: string;
    createdBy?: string;
    createdDate?: Moment;
    lastModifiedBy?: string;
    lastModifiedDate?: Moment;
    orders?: IOrders[];
    customerCustomerName?: string;
    customerId?: number;
    supplierSupplierName?: string;
    supplierId?: number;
    countryCountryName?: string;
    countryId?: number;
}

export class CustomerAddresses implements ICustomerAddresses {
    constructor(
        public id?: number,
        public addressReference?: string,
        public addressName?: string,
        public addressNif?: string,
        public streetAddress?: string,
        public city?: string,
        public state?: string,
        public zipCode?: string,
        public phoneNumber?: string,
        public createdBy?: string,
        public createdDate?: Moment,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Moment,
        public orders?: IOrders[],
        public customerCustomerName?: string,
        public customerId?: number,
        public supplierSupplierName?: string,
        public supplierId?: number,
        public countryCountryName?: string,
        public countryId?: number
    ) {}
}
