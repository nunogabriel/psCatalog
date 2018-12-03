import { Moment } from 'moment';
import { IAddresses } from 'app/shared/model//addresses.model';
import { IProducts } from 'app/shared/model//products.model';

export interface ISuppliers {
    id?: number;
    supplierName?: string;
    supplierNif?: string;
    supplierEmail?: string;
    supplierPhone?: string;
    supplierBeginDate?: Moment;
    supplierEndDate?: Moment;
    createdBy?: string;
    createdDate?: Moment;
    lastModifiedBy?: string;
    lastModifiedDate?: Moment;
    addresses?: IAddresses[];
    products?: IProducts[];
}

export class Suppliers implements ISuppliers {
    constructor(
        public id?: number,
        public supplierName?: string,
        public supplierNif?: string,
        public supplierEmail?: string,
        public supplierPhone?: string,
        public supplierBeginDate?: Moment,
        public supplierEndDate?: Moment,
        public createdBy?: string,
        public createdDate?: Moment,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Moment,
        public addresses?: IAddresses[],
        public products?: IProducts[]
    ) {}
}
